package com.fangchen.oj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangchen.oj.common.ErrorCode;
import com.fangchen.oj.constant.CommonConstant;
import com.fangchen.oj.exception.BusinessException;
import com.fangchen.oj.judge.JudgeService;
import com.fangchen.oj.model.dto.problemsubmit.ProblemSubmitAddRequest;
import com.fangchen.oj.model.dto.problemsubmit.ProblemSubmitQueryRequest;
import com.fangchen.oj.model.entity.Problem;
import com.fangchen.oj.model.entity.ProblemSubmit;
import com.fangchen.oj.model.entity.User;
import com.fangchen.oj.model.enums.ProblemSubmitLanguageEnum;
import com.fangchen.oj.model.enums.ProblemSubmitStatusEnum;
import com.fangchen.oj.model.vo.ProblemSubmitVO;
import com.fangchen.oj.service.ProblemService;
import com.fangchen.oj.service.ProblemSubmitService;
import com.fangchen.oj.mapper.ProblemSubmitMapper;
import com.fangchen.oj.service.UserService;
import com.fangchen.oj.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author fangchen.ye
* @description 针对表【problem_submit(题目提交表)】的数据库操作Service实现
* @createDate 2024-03-26 17:48:43
*/
@Service
public class ProblemSubmitServiceImpl extends ServiceImpl<ProblemSubmitMapper, ProblemSubmit> implements ProblemSubmitService {
    @Resource
    private ProblemService problemService;

    @Resource
    private UserService userService;

    @Lazy
    @Resource
    private JudgeService judgeService;

    /**
     * 题目提交
     *
     * @param problemSubmitAddRequest, loginUser
     * @return problemSubmitId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long doProblemSubmit(ProblemSubmitAddRequest problemSubmitAddRequest, User loginUser) {
        // 判断语言是否合法
        ProblemSubmitLanguageEnum language = ProblemSubmitLanguageEnum.getEnumByValue(problemSubmitAddRequest.getLanguage());
        System.out.println("language: " + language);
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
        problemSubmit.setLanguage(language.getValue());
        problemSubmit.setStatus(0);
        problemSubmit.setJudgeResult("{}");
        boolean save = this.save(problemSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Submit Error");
        }
        // todo: judge
        Long problemSubmitId = problemSubmit.getId();
//        CompletableFuture.runAsync(() -> {
//            judgeService.doJudge(problemSubmitId);
//        });
        judgeService.doJudge(problemSubmitId);
        return problemSubmitId;
    }

    /**
     * 封装了事务的方法
     *
     * @param userId, problemId
     * @return success
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


    @Override
    public QueryWrapper<ProblemSubmit> getQueryWrapper(ProblemSubmitQueryRequest problemSubmitQueryRequest) {
        QueryWrapper<ProblemSubmit> queryWrapper = new QueryWrapper<>();
        if (problemSubmitQueryRequest == null) {
            return queryWrapper;
        }

        Long problemId = problemSubmitQueryRequest.getProblemId();
        String language = problemSubmitQueryRequest.getLanguage();
        Integer status = problemSubmitQueryRequest.getStatus();
        Long userId = problemSubmitQueryRequest.getUserId();
        String sortField = problemSubmitQueryRequest.getSortField();
        String sortOrder = problemSubmitQueryRequest.getSortOrder();

        queryWrapper.eq(ObjectUtils.isNotEmpty(problemId), "problemId", problemId)
                .eq(StringUtils.isNotBlank(language), "language", language)
                .eq(ProblemSubmitStatusEnum.getEnumByValue(status) != null, "status", status)
                .eq(ObjectUtils.isNotEmpty(userId), "userId", userId)
                .eq("isDelete", false)
                .orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    @Override
    public ProblemSubmitVO getProblemSubmitVO(ProblemSubmit problemSubmit, User loginUser) {
        ProblemSubmitVO problemSubmitVO = ProblemSubmitVO.objToVo(problemSubmit);
        // filter
        long userId = loginUser.getId();
        if (userId != problemSubmit.getUserId() && !userService.isAdmin(loginUser)) {
            problemSubmitVO.setCode(null);
        }
        return problemSubmitVO;
    }

    @Override
    public Page<ProblemSubmitVO> getProblemSubmitVOPage(Page<ProblemSubmit> problemSubmitPage, User loginUser) {
        QueryWrapper<ProblemSubmit> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("createTime");

        // 执行分页查询
        Page<ProblemSubmit> resultPage = this.page(problemSubmitPage, queryWrapper);

        // 将结果转换为 ProblemSubmitVO
        List<ProblemSubmitVO> problemSubmitVOList = resultPage.getRecords().stream()
                .map(problemSubmit -> getProblemSubmitVO(problemSubmit, loginUser))
                .collect(Collectors.toList());

        // 创建并返回分页结果
        Page<ProblemSubmitVO> problemSubmitVOPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        problemSubmitVOPage.setRecords(problemSubmitVOList);
        return problemSubmitVOPage;
    }
}




