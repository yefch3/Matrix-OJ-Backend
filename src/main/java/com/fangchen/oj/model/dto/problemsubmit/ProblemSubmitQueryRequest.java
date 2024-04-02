package com.fangchen.oj.model.dto.problemsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class ProblemSubmitQueryRequest implements Serializable {
    /**
     * 题目 id
     */
    private Long problemId;

    /**
     * 语言
     */
    private String language;


    /**
     * 状态
     */
    private Integer status;

    /**
     * 用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;

}