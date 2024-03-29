package com.fangchen.oj.service;

import com.fangchen.oj.model.dto.problemsubmit.ProblemSubmitAddRequest;
import com.fangchen.oj.model.entity.ProblemSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fangchen.oj.model.entity.User;
import org.springframework.web.bind.annotation.RequestBody;

/**
* @author fangchen.ye
* @description 针对表【problem_submit(题目提交表)】的数据库操作Service
* @createDate 2024-03-26 17:48:43
*/
public interface ProblemSubmitService extends IService<ProblemSubmit> {
    /**
     * 题目提交
     *
     * @param problemSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doProblemSubmit(ProblemSubmitAddRequest problemSubmitAddRequest, User loginUser);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId
     * @param postId
     * @return
     */
    int doProblemSubmitInner(long userId, long postId);
}
