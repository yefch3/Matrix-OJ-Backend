package com.fangchen.oj.model.dto.problem;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fangchen.oj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询请求
 *
 * @author <a href="https://www.linkedin.com/in/fangchen-ye/i">fangchen</a>
 * @from <a href="https://www.linkedin.com/in/fangchen-ye/">fangchen</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProblemQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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
     * 创建用户 id
     */
    private Long userId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}