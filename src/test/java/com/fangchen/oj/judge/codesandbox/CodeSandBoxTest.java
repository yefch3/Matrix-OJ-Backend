package com.fangchen.oj.judge.codesandbox;

import com.fangchen.oj.judge.codesandbox.model.ExecuteCodeRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CodeSandBoxTest {
    @Value("${codesandbox.type}")
    private String type;

    @Test
    void executeCode() {
        String type = "example";
        CodeSandBox codeSandBox = CodeSandBoxFactory.createCodeSandBox(type);
        String code = "int main() { return 0; }";
        List<String> inputList = Arrays.asList("1", "2", "3");
        String language = "cpp";
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .inputList(inputList)
                .language(language)
                .build();
        codeSandBox.executeCode(executeCodeRequest);
        assertNotNull(codeSandBox);
    }

    @Test
    void executeCodeByConfig() {
        CodeSandBox codeSandBox = CodeSandBoxFactory.createCodeSandBox(type);
        String code = "int main() { return 0; }";
        List<String> inputList = Arrays.asList("1", "2", "3");
        String language = "cpp";
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .inputList(inputList)
                .language(language)
                .build();
        codeSandBox.executeCode(executeCodeRequest);
        assertNotNull(codeSandBox);
    }

    @Test
    void executeCodeByProxy() {
        CodeSandBox codeSandBox = CodeSandBoxFactory.createCodeSandBox(type);
        CodeSandBoxProxy codeSandBoxProxy = new CodeSandBoxProxy(codeSandBox);
        String code = "int main() { return 0; }";
        List<String> inputList = Arrays.asList("1", "2", "3");
        String language = "cpp";
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .inputList(inputList)
                .language(language)
                .build();
        codeSandBoxProxy.executeCode(executeCodeRequest);
        assertNotNull(codeSandBoxProxy);
    }
}