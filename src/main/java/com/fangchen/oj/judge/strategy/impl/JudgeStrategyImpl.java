package com.fangchen.oj.judge.strategy.impl;

import cn.hutool.json.JSONUtil;
import com.fangchen.oj.judge.strategy.JudgeStrategy;
import com.fangchen.oj.judge.strategy.model.JudgeContext;
import com.fangchen.oj.model.dto.problem.JudgeCase;
import com.fangchen.oj.model.dto.problem.JudgeConfig;
import com.fangchen.oj.model.dto.problemsubmit.JudgeResult;
import com.fangchen.oj.model.entity.Problem;
import com.fangchen.oj.model.enums.ProblemSubmitJudgeResultEnum;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JudgeStrategyImpl implements JudgeStrategy {

    @Override
    public JudgeResult executeStrategy(JudgeContext judgeContext) {
        JudgeResult judgeResult = judgeContext.getJudgeResult();
        List<String> outputList = judgeContext.getOutputList();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        Problem problem = judgeContext.getProblem();

        // 如果没有输出或者一些情况下，说明出现了编译错误或者系统错误等等，todo: 需要根据具体情况进行判断，暂时归结为编译错误
        if (outputList == null || outputList.isEmpty()) {
            judgeResult.setResult(ProblemSubmitJudgeResultEnum.COMPILE_ERROR.getValue());
            return judgeResult;
        }

        // 判断输出size和测试用例size是否一致
        if (outputList.size() != judgeCaseList.size()) {
            judgeResult.setResult(ProblemSubmitJudgeResultEnum.WRONG_ANSWER.getValue());
            return judgeResult;
        }

        // 判断每个输出是否和测试用例输出一致
        for (int i = 0; i < outputList.size(); i++) {
            if (!outputList.get(i).equals(judgeCaseList.get(i).getOutput())) {
                judgeResult.setResult(ProblemSubmitJudgeResultEnum.WRONG_ANSWER.getValue());
            }
        }

        // 判断题目限制条件是否满足
        String judgeConfigStr = problem.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        if (judgeResult.getTime() > judgeConfig.getTimeLimit()) {
            judgeResult.setResult(ProblemSubmitJudgeResultEnum.TIME_LIMIT_EXCEEDED.getValue());
        }
        if (judgeResult.getMemory() > judgeConfig.getMemoryLimit()) {
            judgeResult.setResult(ProblemSubmitJudgeResultEnum.MEMORY_LIMIT_EXCEEDED.getValue());
        }

        // 三者都满足则返回AC
        judgeResult.setResult(ProblemSubmitJudgeResultEnum.ACCEPTED.getValue());
        return judgeResult;
    }
}
