package com.fangchen.oj.judge.codesandbox.impl;

import com.fangchen.oj.judge.codesandbox.CodeSandBox;
import com.fangchen.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.fangchen.oj.judge.codesandbox.model.ExecuteCodeResponse;

public class RemoteCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("RemoteCodeSandBox executeCode");
        return null;
    }
}
