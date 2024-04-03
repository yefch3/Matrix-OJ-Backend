package com.fangchen.oj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fangchen.oj.annotation.AuthCheck;
import com.fangchen.oj.common.BaseResponse;
import com.fangchen.oj.common.ErrorCode;
import com.fangchen.oj.common.ResultUtils;
import com.fangchen.oj.constant.UserConstant;
import com.fangchen.oj.exception.BusinessException;
import com.fangchen.oj.model.dto.post.PostQueryRequest;
import com.fangchen.oj.model.dto.problemsubmit.ProblemSubmitAddRequest;
import com.fangchen.oj.model.dto.problemsubmit.ProblemSubmitQueryRequest;
import com.fangchen.oj.model.entity.Post;
import com.fangchen.oj.model.entity.ProblemSubmit;
import com.fangchen.oj.model.entity.User;
import com.fangchen.oj.model.vo.ProblemSubmitVO;
import com.fangchen.oj.service.ProblemSubmitService;
import com.fangchen.oj.service.UserService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 题目提交接口
 *
 * @author <a href="https://www.linkedin.com/in/fangchen-ye/i">fangchen</a>
 * @from <a href="https://www.linkedin.com/in/fangchen-ye/">fangchen</a>
 */
@RestController
@RequestMapping("/problem_submit")
@Slf4j
public class ProblemSubmitController {

    @Resource
    private ProblemSubmitService problemSubmitService;

    @Resource
    private UserService userService;

    /**
     * 题目提交
     *
     * @param problemSubmitAddRequest, request
     * @return resultNum 题目提交ID
     */
    @PostMapping("/")
    public BaseResponse<Long> doProblemSubmit(@RequestBody ProblemSubmitAddRequest problemSubmitAddRequest,
                                         HttpServletRequest request) {
        if (problemSubmitAddRequest == null || problemSubmitAddRequest.getProblemId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long result = problemSubmitService.doProblemSubmit(problemSubmitAddRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 题目提交列表
     *
     * @param problemSubmitQueryRequest, request
     * @return Page<ProblemSubmitVO> 题目提交列表
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<ProblemSubmitVO>> listProblemSubmitByPage(@RequestBody ProblemSubmitQueryRequest problemSubmitQueryRequest,
                                                                       HttpServletRequest request) {
        long current = problemSubmitQueryRequest.getCurrent();
        long size = problemSubmitQueryRequest.getPageSize();
        final User loginUser = userService.getLoginUser(request);
        // 从数据库中查询原始的题目提交分页信息
        Page<ProblemSubmit> problemSubmitPage = problemSubmitService.page(new Page<>(current, size),
                problemSubmitService.getQueryWrapper(problemSubmitQueryRequest));
        // filter
        return ResultUtils.success(problemSubmitService.getProblemSubmitVOPage(problemSubmitPage, loginUser));
    }

}
