package com.fangchen.oj.judge.codesandbox.impl;

import com.fangchen.oj.judge.codesandbox.CodeSandBox;
import com.fangchen.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.fangchen.oj.judge.codesandbox.model.ExecuteCodeResponse;

public class ExampleCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("ExampleCodeSandBox execute code");
        return null;
    }
}
