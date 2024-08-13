package com.fangchen.oj.judge;

import cn.hutool.json.JSONUtil;
import com.fangchen.oj.common.ErrorCode;
import com.fangchen.oj.exception.BusinessException;
import com.fangchen.oj.judge.codesandbox.CodeSandBox;
import com.fangchen.oj.judge.codesandbox.CodeSandBoxFactory;
import com.fangchen.oj.judge.codesandbox.CodeSandBoxProxy;
import com.fangchen.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.fangchen.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.fangchen.oj.judge.strategy.JudgeStrategy;
import com.fangchen.oj.judge.strategy.model.JudgeContext;
import com.fangchen.oj.model.dto.problem.JudgeCase;
import com.fangchen.oj.model.dto.problem.JudgeConfig;
import com.fangchen.oj.model.dto.problemsubmit.JudgeResult;
import com.fangchen.oj.model.entity.Problem;
import com.fangchen.oj.model.entity.ProblemSubmit;
import com.fangchen.oj.model.enums.ProblemSubmitJudgeResultEnum;
import com.fangchen.oj.model.enums.ProblemSubmitStatusEnum;
import com.fangchen.oj.service.ProblemService;
import com.fangchen.oj.service.ProblemSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class JudgeServiceImpl implements JudgeService {

    @Value("${codesandbox.type}")
    private String type;

    @Resource
    private ProblemService problemService;

    @Resource
    private ProblemSubmitService problemSubmitService;

    @Resource
    private JudgeStrategy judgeStrategy;

    @Override
    public void doJudge(long problemSubmitId) {

        // 每次提交会有一个新的problemSubmitId，根据这个id获取提交记录
        ProblemSubmit problemSubmit = problemSubmitService.getById(problemSubmitId);
        if (problemSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交记录不存在");
        }

        // 获取题目提交详细信息
        Long problemId = problemSubmit.getProblemId();
        String language = problemSubmit.getLanguage();
        String code = problemSubmit.getCode();
        Integer status = problemSubmit.getStatus();

        // 获取题目信息
        Problem problem = problemService.getById(problemId);
        JudgeConfig judgeConfig = JSONUtil.toBean(problem.getJudgeConfig(), JudgeConfig.class);
        long timiLimit = judgeConfig.getTimeLimit();
        long memoryLimit = judgeConfig.getMemoryLimit();

        // 获取测试用例信息
        String judgeCaseStr = problem.getJudgeCase();
        List<JudgeCase> caseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);

        // 判断题目提交状态，如果不是等待判题状态，则不进行判题
        if (!status.equals(ProblemSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中，请稍后再试");
        }

        // 更新题目提交状态为判题中
        problemSubmit.setStatus(ProblemSubmitStatusEnum.JUDGING.getValue());
        boolean problemSubmitUpdate = problemSubmitService.updateById(problemSubmit);
        if (!problemSubmitUpdate) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新提交信息失败");
        }

        // 调用执行核心代码，获取执行结果
        // todo: inputList需要根据题目信息获取，最简单的就是根据题目id获取对应的输入输出文件，暂时从测试用例中获取
        CodeSandBox codeSandBox = CodeSandBoxFactory.createCodeSandBox(type);
        CodeSandBoxProxy codeSandBoxProxy = new CodeSandBoxProxy(codeSandBox);
        List<String> inputList = caseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .inputList(inputList)
                .language(language)
                .timeLimit(timiLimit)
                .memoryLimit(memoryLimit)
                .build();

        // 调用沙箱执行代码
        ExecuteCodeResponse executeCodeResponse = codeSandBoxProxy.executeCode(executeCodeRequest);

        // 如果执行结果为空，则执行代码失败
        if (executeCodeResponse == null) {
            problemSubmit.setStatus(ProblemSubmitStatusEnum.FAILED.getValue());
            problemSubmitUpdate = problemSubmitService.updateById(problemSubmit);
            if (!problemSubmitUpdate) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新提交信息失败");
            }
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "判题失败");
        }


        // 更新题目提交状态为执行代码完成
        problemSubmit.setStatus(ProblemSubmitStatusEnum.SUCCEED.getValue());

        // 根据执行结果，判断是否通过，即执行判题策略，在这个策略接口中会修改judgeResult的result
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setExecuteCodeResponse(executeCodeResponse);
        judgeContext.setJudgeCaseList(caseList);
        judgeContext.setProblem(problem);
        JudgeResult judgeResult = judgeStrategy.executeStrategy(judgeContext);

        if (judgeResult.getResult().equals(ProblemSubmitJudgeResultEnum.ACCEPTED.getValue())) {
            problem.setAcceptNum(problem.getAcceptNum() + 1);
        }
        problem.setSubmitNum(problem.getSubmitNum() + 1);
        boolean problemUpdate = problemService.updateById(problem);
        if (!problemUpdate) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新题目信息失败");
        }

        problemSubmit.setJudgeResult(JSONUtil.toJsonStr(judgeResult));
        problemSubmitUpdate =  problemSubmitService.updateById(problemSubmit);
        if (!problemSubmitUpdate) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新提交信息失败");
        }
    }
}
