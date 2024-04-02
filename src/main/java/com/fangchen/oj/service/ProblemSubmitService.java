package com.fangchen.oj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fangchen.oj.model.dto.problem.ProblemQueryRequest;
import com.fangchen.oj.model.dto.problemsubmit.ProblemSubmitAddRequest;
import com.fangchen.oj.model.dto.problemsubmit.ProblemSubmitQueryRequest;
import com.fangchen.oj.model.entity.Problem;
import com.fangchen.oj.model.entity.ProblemSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fangchen.oj.model.entity.User;
import com.fangchen.oj.model.vo.ProblemSubmitVO;
import com.fangchen.oj.model.vo.ProblemVO;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * 获取查询条件
     *
     * @param problemSubmitQueryRequest
     * @return
     */
    QueryWrapper<ProblemSubmit> getQueryWrapper(ProblemSubmitQueryRequest problemSubmitQueryRequest);

    /**
     * 获取问题提交视图
     *
     * @param problemSubmit
     * @param loginUser
     * @return
     */
    ProblemSubmitVO getProblemSubmitVO(ProblemSubmit problemSubmit, User loginUser);

    /**
     * 获取问题提交视图分页
     *
     * @param problemSubmitPage
     * @param loginUser
     * @return
     */
    Page<ProblemSubmitVO> getProblemSubmitVOPage(Page<ProblemSubmit> problemSubmitPage, User loginUser);
}
