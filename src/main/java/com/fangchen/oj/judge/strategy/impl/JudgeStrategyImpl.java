package com.fangchen.oj.judge.strategy.impl;

import cn.hutool.json.JSONUtil;
import com.fangchen.oj.judge.codesandbox.model.CmdMessageEnum;
import com.fangchen.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.fangchen.oj.judge.strategy.JudgeStrategy;
import com.fangchen.oj.judge.strategy.model.JudgeContext;
import com.fangchen.oj.model.dto.problem.JudgeCase;
import com.fangchen.oj.model.dto.problem.JudgeConfig;
import com.fangchen.oj.model.dto.problemsubmit.JudgeResult;
import com.fangchen.oj.model.entity.Problem;
import com.fangchen.oj.model.enums.ProblemSubmitJudgeResultEnum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class JudgeStrategyImpl implements JudgeStrategy {

    @Override
    public JudgeResult executeStrategy(JudgeContext judgeContext) {
        ExecuteCodeResponse executeCodeResponse = judgeContext.getExecuteCodeResponse();
        List<String> outputList = executeCodeResponse.getOutputList();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        Problem problem = judgeContext.getProblem();
        long timeLimit = JSONUtil.toBean(problem.getJudgeConfig(), JudgeConfig.class).getTimeLimit();
        long memoryLimit = JSONUtil.toBean(problem.getJudgeConfig(), JudgeConfig.class).getMemoryLimit() * 1024 * 1024;
        long timeMax = 0;
        long memoryMax = 0;


        JudgeResult judgeResult = new JudgeResult();
        // 编译失败
        if (Objects.equals(executeCodeResponse.getExitValue(), CmdMessageEnum.COMPILE_ERROR.getValue())) {
            judgeResult.setResult(ProblemSubmitJudgeResultEnum.COMPILE_ERROR.getValue());
            judgeResult.setMessage(executeCodeResponse.getMessage());
            return judgeResult;
        }

        // 运行失败
        if (Objects.equals(executeCodeResponse.getExitValue(), CmdMessageEnum.RUNTIME_ERROR.getValue())) {
            judgeResult.setResult(ProblemSubmitJudgeResultEnum.RUNTIME_ERROR.getValue());
            judgeResult.setMessage(executeCodeResponse.getMessage());
            return judgeResult;
        }

        // 运行成功，但是输出长度不对
        if (outputList.size() != judgeCaseList.size()) {
            judgeResult.setResult(ProblemSubmitJudgeResultEnum.WRONG_ANSWER.getValue());
            judgeResult.setMessage("Wrong Output Size");
            return judgeResult;
        }

        // 错误输出，超时，超内存的情况
        for (int i = 0; i < outputList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            String output = outputList.get(i);
            timeMax = Math.max(timeMax, executeCodeResponse.getTimeList().get(i));
            memoryMax = Math.max(memoryMax, executeCodeResponse.getMemoryList().get(i));
            if (!Objects.equals(output, judgeCase.getOutput())) {
                judgeResult.setResult(ProblemSubmitJudgeResultEnum.WRONG_ANSWER.getValue());
                judgeResult.setMessage("Wrong Answer in Case " + (i + 1));
                return judgeResult;
            } else if (timeLimit < executeCodeResponse.getTimeList().get(i)) {
                judgeResult.setResult(ProblemSubmitJudgeResultEnum.TIME_LIMIT_EXCEEDED.getValue());
                judgeResult.setMessage("Time Limit Exceeded in Case " + (i + 1));
                return judgeResult;
            } else if (memoryLimit < executeCodeResponse.getMemoryList().get(i)) {
                judgeResult.setResult(ProblemSubmitJudgeResultEnum.MEMORY_LIMIT_EXCEEDED.getValue());
                judgeResult.setMessage("Memory Limit Exceeded in Case " + (i + 1));
                return judgeResult;
            }
        }

        judgeResult.setResult(ProblemSubmitJudgeResultEnum.ACCEPTED.getValue());
        judgeResult.setMessage("Accepted");
        judgeResult.setTime(timeMax);
        judgeResult.setMemory(memoryMax);

        return judgeResult;
    }
}
