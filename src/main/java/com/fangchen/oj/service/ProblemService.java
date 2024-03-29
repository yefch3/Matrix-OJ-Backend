package com.fangchen.oj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fangchen.oj.model.dto.problem.ProblemQueryRequest;
import com.fangchen.oj.model.entity.Problem;
import com.fangchen.oj.model.entity.Problem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fangchen.oj.model.vo.ProblemVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author fangchen.ye
* @description 针对表【problem(题目表)】的数据库操作Service
* @createDate 2024-03-26 17:45:04
*/
public interface ProblemService extends IService<Problem> {
    /**
     * 校验
     *
     * @param problem
     * @param add
     */
    void validProblem(Problem problem, boolean add);

    /**
     * 获取查询条件
     *
     * @param problemQueryRequest
     * @return
     */
    QueryWrapper<Problem> getQueryWrapper(ProblemQueryRequest problemQueryRequest);

//    /**
//     * 从 ES 查询
//     *
//     * @param problemQueryRequest
//     * @return
//     */
//    Page<Problem> searchFromEs(ProblemQueryRequest problemQueryRequest);

    /**
     * 获取题目封装
     *
     * @param problem
     * @param request
     * @return
     */
    ProblemVO getProblemVO(Problem problem, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param problemPage
     * @param request
     * @return
     */
    Page<ProblemVO> getProblemVOPage(Page<Problem> problemPage, HttpServletRequest request);
}
