package com.fangchen.oj.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.fangchen.oj.judge.codesandbox.CodeSandBox;
import com.fangchen.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.fangchen.oj.judge.codesandbox.model.ExecuteCodeResponse;

public class RemoteCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
//        System.out.println("RemoteCodeSandBox execute code");
        String url = "3.128.50.111:8080/execute";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
//        System.out.println("request json: " + json);
        String result = HttpUtil.post(url, json);
//        System.out.println("response json: " + result);
        return JSONUtil.toBean(result, ExecuteCodeResponse.class);
    }
}
