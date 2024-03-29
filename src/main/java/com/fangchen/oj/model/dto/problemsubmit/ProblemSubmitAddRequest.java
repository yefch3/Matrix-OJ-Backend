package com.fangchen.oj.model.dto.problemsubmit;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class ProblemSubmitAddRequest implements Serializable {
    /**
     * 题目 id
     */
    private Long problemId;

    /**
     * 语言
     */
    private String language;

    /**
     * 提交代码
     */
    private String code;

    private static final long serialVersionUID = 1L;

}