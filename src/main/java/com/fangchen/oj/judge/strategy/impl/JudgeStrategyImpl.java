package com.fangchen.oj.judge.strategy.impl;

import cn.hutool.core.lang.Console;
import cn.hutool.json.JSONUtil;
import com.fangchen.oj.judge.strategy.JudgeStrategy;
import com.fangchen.oj.judge.strategy.model.JudgeContext;
import com.fangchen.oj.model.dto.problem.JudgeCase;
import com.fangchen.oj.model.dto.problem.JudgeConfig;
import com.fangchen.oj.model.dto.problemsubmit.JudgeResult;
import com.fangchen.oj.model.entity.Problem;
import com.fangchen.oj.model.enums.ProblemSubmitJudgeResultEnum;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JudgeStrategyImpl implements JudgeStrategy {

    @Override
    public JudgeResult executeStrategy(JudgeContext judgeContext) {
        JudgeResult judgeResult = judgeContext.getJudgeResult();
        List<String> outputList = judgeContext.getOutputList();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        Problem problem = judgeContext.getProblem();

        // 如果没有输出或者一些情况下，说明出现了编译错误或者系统错误等等，todo: 需要根据具体情况进行判断，暂时归结为编译错误
        if (outputList == null || outputList.isEmpty()) {
//            Console.log("编译错误");
            judgeResult.setResult(ProblemSubmitJudgeResultEnum.COMPILE_ERROR.getValue());
            return judgeResult;
        }

        // 判断输出size和测试用例size是否一致
        if (outputList.size() != judgeCaseList.size()) {
//            Console.log("测试用例数量和实际输出数量不一致");
            judgeResult.setResult(ProblemSubmitJudgeResultEnum.WRONG_ANSWER.getValue());
            return judgeResult;
        }

        // 判断每个输出是否和测试用例输出一致
        for (int i = 0; i < outputList.size(); i++) {
//            Console.log("output: " + outputList.get(i));
//            Console.log("judgeCase: " + judgeCaseList.get(i).getOutput());
            if (!outputList.get(i).equals(judgeCaseList.get(i).getOutput())) {
//                System.out.println("测试用例输出和实际输出不一致");
                judgeResult.setResult(ProblemSubmitJudgeResultEnum.WRONG_ANSWER.getValue());
                return judgeResult;
            }
        }

        // 判断题目限制条件是否满足
        String judgeConfigStr = problem.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        if (judgeResult.getTime() > judgeConfig.getTimeLimit()) {
//            Console.log("时间超限");
            judgeResult.setResult(ProblemSubmitJudgeResultEnum.TIME_LIMIT_EXCEEDED.getValue());
            return judgeResult;
        }
        if (judgeResult.getMemory() > judgeConfig.getMemoryLimit()) {
//            Console.log("内存超限");
            judgeResult.setResult(ProblemSubmitJudgeResultEnum.MEMORY_LIMIT_EXCEEDED.getValue());
            return judgeResult;
        }

        // 三者都满足则返回AC
//        Console.log("Accepted");
        judgeResult.setResult(ProblemSubmitJudgeResultEnum.ACCEPTED.getValue());
        return judgeResult;
    }
}
