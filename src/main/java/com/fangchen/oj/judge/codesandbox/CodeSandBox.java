package com.fangchen.oj.judge.codesandbox;

import com.fangchen.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.fangchen.oj.judge.codesandbox.model.ExecuteCodeResponse;

public interface CodeSandBox {
    /**
     * Execute code in CodeSandBox
     * @return ExecuteCodeResponse
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
