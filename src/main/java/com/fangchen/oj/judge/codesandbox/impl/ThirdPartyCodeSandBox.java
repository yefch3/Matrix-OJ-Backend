package com.fangchen.oj.judge.codesandbox.impl;

import com.fangchen.oj.judge.codesandbox.CodeSandBox;
import com.fangchen.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.fangchen.oj.judge.codesandbox.model.ExecuteCodeResponse;

public class ThirdPartyCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("Executing code in third party code sandbox");
        return null;
    }
}
