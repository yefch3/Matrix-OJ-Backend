package com.fangchen.oj.judge.codesandbox.model;

import com.fangchen.oj.model.dto.problemsubmit.JudgeResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecuteCodeResponse {
    private List<String> outputList;

    private String apiMessage;

    private JudgeResult judgeResult;
}
