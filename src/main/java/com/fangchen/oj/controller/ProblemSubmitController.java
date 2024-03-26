package com.fangchen.oj.controller;

import com.fangchen.oj.common.BaseResponse;
import com.fangchen.oj.common.ErrorCode;
import com.fangchen.oj.common.ResultUtils;
import com.fangchen.oj.exception.BusinessException;
import com.fangchen.oj.model.dto.postthumb.ProblemSubmitAddRequest;
import com.fangchen.oj.model.entity.User;
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
     * 点赞 / 取消点赞
     *
     * @param problemSubmitAddRequest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doThumb(@RequestBody ProblemSubmitAddRequest problemSubmitAddRequest,
                                         HttpServletRequest request) {
        if (problemSubmitAddRequest == null || problemSubmitAddRequest.getProblemId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long postId = problemSubmitAddRequest.getProblemId();
        int result = problemSubmitService.doProblemSubmit(postId, loginUser);
        return ResultUtils.success(result);
    }

}
