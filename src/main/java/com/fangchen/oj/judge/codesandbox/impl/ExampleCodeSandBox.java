package com.fangchen.oj.judge.codesandbox.impl;

import com.fangchen.oj.judge.codesandbox.CodeSandBox;
import com.fangchen.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.fangchen.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.fangchen.oj.model.dto.problemsubmit.JudgeResult;
import com.fangchen.oj.model.enums.ProblemSubmitJudgeResultEnum;
import com.fangchen.oj.model.enums.ProblemSubmitStatusEnum;

import java.util.ArrayList;
import java.util.List;

public class ExampleCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("ExampleCodeSandBox execute code");
        List<String> outputList = new ArrayList<>();
        outputList.add("Hello World");
        String message = "No Probelm";
        JudgeResult judgeResult = new JudgeResult("Run Successfully", ProblemSubmitJudgeResultEnum.WAITING.getValue(), 100L, 100L);
        return new ExecuteCodeResponse(outputList, message, judgeResult);
    }
}
