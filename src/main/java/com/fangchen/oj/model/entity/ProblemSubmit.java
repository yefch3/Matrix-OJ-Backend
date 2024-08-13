package com.fangchen.oj.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 题目提交表
 * @TableName problem_submit
 */
@TableName(value ="problem_submit")
@Data
public class ProblemSubmit implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
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
    // 这里仅仅指是否成功调用了判题接口进行了判题服务，和判题结果是否正确无关
    private Integer status;

    /**
     * 判题结果 （json对象）
     */
    private String judgeResult;

    /**
     * 提交时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}