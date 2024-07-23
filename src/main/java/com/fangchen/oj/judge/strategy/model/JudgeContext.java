package com.fangchen.oj.judge.strategy.model;

import com.fangchen.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.fangchen.oj.model.dto.problem.JudgeCase;
import com.fangchen.oj.model.dto.problemsubmit.JudgeResult;
import com.fangchen.oj.model.entity.Problem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeContext {

    private ExecuteCodeResponse executeCodeResponse;

    private List<JudgeCase> judgeCaseList;

    private Problem problem;
}
