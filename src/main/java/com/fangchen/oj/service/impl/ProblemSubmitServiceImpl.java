package com.fangchen.oj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangchen.oj.common.ErrorCode;
import com.fangchen.oj.exception.BusinessException;
import com.fangchen.oj.model.dto.problemsubmit.ProblemSubmitAddRequest;
import com.fangchen.oj.model.entity.Problem;
import com.fangchen.oj.model.entity.ProblemSubmit;
import com.fangchen.oj.model.entity.User;
import com.fangchen.oj.model.enums.ProblemSubmitLanguageEnum;
import com.fangchen.oj.service.ProblemService;
import com.fangchen.oj.service.ProblemSubmitService;
import com.fangchen.oj.mapper.ProblemSubmitMapper;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;

/**
* @author fangchen.ye
* @description 针对表【problem_submit(题目提交表)】的数据库操作Service实现
* @createDate 2024-03-26 17:48:43
*/
@Service
public class ProblemSubmitServiceImpl extends ServiceImpl<ProblemSubmitMapper, ProblemSubmit> implements ProblemSubmitService {
    @Resource
    private ProblemService problemService;

    /**
     * 题目提交
     *
     * @param problemSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doProblemSubmit(ProblemSubmitAddRequest problemSubmitAddRequest, User loginUser) {
        // 判断语言是否合法
        String language = problemSubmitAddRequest.getLanguage();
        ProblemSubmitLanguageEnum.getEnumByValue(language);
        if (language == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Wrong Language");

        }
        long problemId = problemSubmitAddRequest.getProblemId();
        // 判断实体是否存在，根据类别获取实体
        Problem problem = problemService.getById(problemId);
        if (problem == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        long userId = loginUser.getId();
        // 每个用户串行提交题目
        // 锁必须要包裹住事务方法
//        ProblemSubmitService problemSubmitService = (ProblemSubmitService) AopContext.currentProxy();
//        synchronized (String.valueOf(userId).intern()) {
//            return problemSubmitService.doProblemSubmitInner(userId, problemId);
//        }
        ProblemSubmit problemSubmit = new ProblemSubmit();
        problemSubmit.setUserId(userId);
        problemSubmit.setProblemId(problemId);
        problemSubmit.setCode(problemSubmitAddRequest.getCode());
        problemSubmit.setLanguage(language);
        problemSubmit.setStatus(0);
        problemSubmit.setJudgeResult("{}");
        boolean save = this.save(problemSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Submit Error");
        }
        return problemSubmit.getId();
    }

    /**
     * 封装了事务的方法
     *
     * @param userId
     * @param problemId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doProblemSubmitInner(long userId, long problemId) {
        ProblemSubmit problemSubmit = new ProblemSubmit();
        problemSubmit.setUserId(userId);
        problemSubmit.setProblemId(problemId);
        QueryWrapper<ProblemSubmit> submitQueryWrapper = new QueryWrapper<>(problemSubmit);
        ProblemSubmit oldProblemSubmit = this.getOne(submitQueryWrapper);
        boolean result;
        // 已点赞
        if (oldProblemSubmit != null) {
            result = this.remove(submitQueryWrapper);
            if (result) {
                // 点赞数 - 1
                result = problemService.update()
                        .eq("id", problemId)
                        .gt("submitNum", 0)
                        .setSql("submitNum = submitNum - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未点赞
            result = this.save(problemSubmit);
            if (result) {
                // 点赞数 + 1
                result = problemService.update()
                        .eq("id", problemId)
                        .setSql("submitNum = submitNum + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }
}




