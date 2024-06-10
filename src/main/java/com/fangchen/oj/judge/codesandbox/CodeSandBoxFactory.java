package com.fangchen.oj.judge.codesandbox;

import com.fangchen.oj.judge.codesandbox.impl.ExampleCodeSandBox;
import com.fangchen.oj.judge.codesandbox.impl.RemoteCodeSandBox;
import com.fangchen.oj.judge.codesandbox.impl.ThirdPartyCodeSandBox;

public class CodeSandBoxFactory {
    public static CodeSandBox createCodeSandBox(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSandBox();
            case "remote":
                return new RemoteCodeSandBox();
            case "thirdParty":
                return new ThirdPartyCodeSandBox();
            default:
                return null;
        }
    }
}
