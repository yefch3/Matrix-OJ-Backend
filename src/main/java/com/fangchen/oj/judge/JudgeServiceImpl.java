package com.fangchen.oj.judge;

import cn.hutool.json.JSONUtil;
import com.fangchen.oj.common.ErrorCode;
import com.fangchen.oj.exception.BusinessException;
import com.fangchen.oj.judge.codesandbox.CodeSandBox;
import com.fangchen.oj.judge.codesandbox.CodeSandBoxFactory;
import com.fangchen.oj.judge.codesandbox.CodeSandBoxProxy;
import com.fangchen.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.fangchen.oj.judge.codesandbox.model.ExecuteCodeResponse;
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

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Value("${judge.type}")
    private String type;

    @Resource
    private ProblemService problemService;

    @Resource
    private ProblemSubmitService problemSubmitService;

    @Override
    public void doJudge(long problemSubmitId) {

        // 每次提交会有一个新的problemSubmitId，根据这个id获取提交记录
        ProblemSubmit problemSubmit = problemSubmitService.getById(problemSubmitId);
        if (problemSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交记录不存在");
        }

        // 获取题目提交详细信息
        Long problemId = problemSubmit.getProblemId();
        Long userId = problemSubmit.getUserId();
        String language = problemSubmit.getLanguage();
        String code = problemSubmit.getCode();
        Integer status = problemSubmit.getStatus();
        String previousJudgeResult = problemSubmit.getJudgeResult();
        Date createTime = problemSubmit.getCreateTime();
        Date updateTime = problemSubmit.getUpdateTime();
        Integer isDelete = problemSubmit.getIsDelete();

        // 获取题目信息
        Problem problem = problemService.getById(problemId);
        if (problem == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目信息不存在");
        }

        // 获取测试用例信息
        String judgeCaseStr = problem.getJudgeCase();
        List<JudgeCase> caseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);

        // 判断题目提交状态，如果不是等待判题状态，则不进行判题
        if (!status.equals(ProblemSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中，请稍后再试");
        }

        // 更新题目提交状态为判题中
        problemSubmit.setStatus(ProblemSubmitStatusEnum.JUDGING.getValue());
        boolean isUpdate = problemSubmitService.updateById(problemSubmit);
        if (!isUpdate) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新提交信息失败");
        }

        // 调用判题核心代码，获取判题结果
        // todo: inputList需要根据题目信息获取，最简单的就是根据题目id获取对应的输入输出文件，暂时从测试用例中获取
        CodeSandBox codeSandBox = CodeSandBoxFactory.createCodeSandBox(type);
        CodeSandBoxProxy codeSandBoxProxy = new CodeSandBoxProxy(codeSandBox);
        List<String> inputList = caseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .inputList(inputList)
                .language(language)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandBoxProxy.executeCode(executeCodeRequest);

        // 如果判题结果为空，则判题失败
        if (executeCodeResponse == null) {
            problemSubmit.setStatus(ProblemSubmitStatusEnum.FAILED.getValue());
            problemSubmitService.updateById(problemSubmit);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "判题失败");
        }

        JudgeResult judgeResult = executeCodeResponse.getJudgeResult();
        Long memory = judgeResult.getMemory();
        Long time = judgeResult.getTime();
        List<String> outputList = executeCodeResponse.getOutputList();
        // 更新题目提交状态为判题完成
        problemSubmit.setStatus(ProblemSubmitStatusEnum.SUCCEED.getValue());

        // 根据执行结果，判断是否通过
        // 1. 判断输出size和测试用例size是否一致
        // 2. 判断每个输出是否和测试用例输出一致
        // 3. 判断题目限制条件是否满足

        // 判断输出size和测试用例size是否一致
        if (outputList.size() != caseList.size()) {
            judgeResult.setMessage(ProblemSubmitJudgeResultEnum.WRONG_ANSWER.getValue());
            problemSubmit.setJudgeResult(JSONUtil.toJsonStr(judgeResult));
            problemSubmitService.updateById(problemSubmit);
            return;
        }

        // 判断每个输出是否和测试用例输出一致
        for (int i = 0; i < outputList.size(); i++) {
            if (!outputList.get(i).equals(caseList.get(i).getOutput())) {
                judgeResult.setMessage(ProblemSubmitJudgeResultEnum.WRONG_ANSWER.getValue());
                problemSubmit.setJudgeResult(JSONUtil.toJsonStr(judgeResult));
                problemSubmitService.updateById(problemSubmit);
                return;
            }
        }

        // 判断题目限制条件是否满足
        String judgeConfigStr = problem.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        if (time > judgeConfig.getTimeLimit()) {
            judgeResult.setMessage(ProblemSubmitJudgeResultEnum.TIME_LIMIT_EXCEEDED.getValue());
            problemSubmit.setJudgeResult(JSONUtil.toJsonStr(judgeResult));
            problemSubmitService.updateById(problemSubmit);
            return;
        }
        if (memory > judgeConfig.getMemoryLimit()) {
            judgeResult.setMessage(ProblemSubmitJudgeResultEnum.MEMORY_LIMIT_EXCEEDED.getValue());
            problemSubmit.setJudgeResult(JSONUtil.toJsonStr(judgeResult));
            problemSubmitService.updateById(problemSubmit);
            return;
        }

        judgeResult.setMessage(ProblemSubmitJudgeResultEnum.ACCEPTED.getValue());
        problemSubmit.setJudgeResult(JSONUtil.toJsonStr(judgeResult));
        problemSubmitService.updateById(problemSubmit);
    }
}
