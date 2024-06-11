package com.fangchen.oj.judge;

import com.fangchen.oj.model.vo.ProblemSubmitVO;

public interface JudgeService {
    /**
     * 判题
     * @param problemSubmitId
     * @return
     */
    void doJudge(long problemSubmitId);
}
