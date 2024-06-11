package com.fangchen.oj.model.dto.problemsubmit;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JudgeResult {
    /**
     * 判题信息，程序执行信息，消耗内存，消耗时间
     */
    private String message;

    private Long time;

    private Long memory;
}
