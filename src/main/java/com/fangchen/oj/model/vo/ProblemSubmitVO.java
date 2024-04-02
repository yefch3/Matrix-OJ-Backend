package com.fangchen.oj.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fangchen.oj.model.dto.problem.JudgeCase;
import com.fangchen.oj.model.dto.problem.JudgeConfig;
import com.fangchen.oj.model.dto.problemsubmit.JudgeResult;
import com.fangchen.oj.model.entity.Problem;
import com.fangchen.oj.model.entity.ProblemSubmit;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 返回给前端用户的问题视图
 */
@Data
public class ProblemSubmitVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 题目 id
     */
    private Long problemId;

    /**
     * 提交用户 id
     */
    private Long userId;

    /**
     * 语言
     */
    private String language;

    /**
     * 提交代码
     */
    private String code;

    /**
     * 状态 （0: 等待判题； 1: 判题中； 2: 成功； 3： 失败）
     */
    private Integer status;

    /**
     * 判题结果 （json对象）
     */
    private JudgeResult judgeResult;

    /**
     * 提交时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 提交用户
     */
    private UserVO userVO;

    /**
     * 题目
     */
    private ProblemVO problemVO;

    /**
     * 包装类转对象
     *
     * @param problemSubmitVO
     * @return
     */
    public static ProblemSubmit voToObj(ProblemSubmitVO problemSubmitVO) {
        if (problemSubmitVO == null) {
            return null;
        }
        ProblemSubmit problemSubmit = new ProblemSubmit();
        BeanUtils.copyProperties(problemSubmitVO, problemSubmit);
        JudgeResult judgeResult = problemSubmitVO.getJudgeResult();
        if (judgeResult != null) {
            problemSubmit.setJudgeResult(JSONUtil.toJsonStr(judgeResult));
        }
        return problemSubmit;
    }

    /**
     * 对象转包装类
     *
     * @param problemSubmit
     * @return
     */
    public static ProblemSubmitVO objToVo(ProblemSubmit problemSubmit) {
        if (problemSubmit == null) {
            return null;
        }
        ProblemSubmitVO problemSubmitVO = new ProblemSubmitVO();
        BeanUtils.copyProperties(problemSubmit, problemSubmitVO);
        String judgeResult = problemSubmit.getJudgeResult();
        if (judgeResult != null) {
            problemSubmitVO.setJudgeResult(JSONUtil.toBean(judgeResult, JudgeResult.class));
        }
        return problemSubmitVO;
    }
}
