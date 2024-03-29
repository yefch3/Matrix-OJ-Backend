package com.fangchen.oj.model.dto.problem;

import lombok.Data;

@Data
public class JudgeConfig {
    /**
     * 评测时间限制，内存限制，栈限制
     */
    private Long timeLimit;

    private Long memoryLimit;

    private Long stackLimit;
}
