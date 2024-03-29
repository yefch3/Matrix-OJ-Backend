package com.fangchen.oj.model.dto.problem;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 创建请求
 *
 * @author <a href="https://www.linkedin.com/in/fangchen-ye/i">fangchen</a>
 * @from <a href="https://www.linkedin.com/in/fangchen-ye/">fangchen</a>
 */
@Data
public class ProblemAddRequest implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 难度
     */
    private Object difficulty;

    /**
     * 答案
     */
    private String answer;

    /**
     * 评测用例 (json 数组)
     */
    private List<JudgeCase> judgeCase;

    /**
     * 评测配置 (json 对象)
     */
    private JudgeConfig judgeConfig;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}