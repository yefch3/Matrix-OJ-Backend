package com.fangchen.oj.model.dto.problemsubmit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeResult {
    /**
     * 判题信息，程序执行信息，消耗内存，消耗时间
     */

    // 具体的判题信息，比如编译错误，运行错误等
    private String message;

    // 这里指的是题目是否通过测试，要用到ProblemSubmitJudgeResultEnum
    private String result;

    private Long time;

    private Long memory;
}
