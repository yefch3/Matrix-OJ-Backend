package com.fangchen.oj.judge.strategy;

import com.fangchen.oj.judge.strategy.model.JudgeContext;
import com.fangchen.oj.model.dto.problemsubmit.JudgeResult;

public interface JudgeStrategy {
    JudgeResult executeStrategy(JudgeContext judgeContext);
}
