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
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>();
        outputList.add("200");
        outputList.add("358");
        List<Long> timeList = new ArrayList<>();
        timeList.add(100L);
        timeList.add(200L);
        List<Long> memoryList = new ArrayList<>();
        memoryList.add(100L);
        memoryList.add(200L);
        executeCodeResponse.setExitValue(0);
        executeCodeResponse.setMemoryList(memoryList);
        executeCodeResponse.setTimeList(timeList);
        executeCodeResponse.setOutputList(outputList);

        return executeCodeResponse;
    }
}
