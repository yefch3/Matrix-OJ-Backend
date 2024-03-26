package com.fangchen.oj.model.dto.problem;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新请求
 *
 * @author <a href="https://www.linkedin.com/in/fangchen-ye/i">fangchen</a>
 * @from <a href="https://www.linkedin.com/in/fangchen-ye/">fangchen</a>
 */
@Data
public class ProblemUpdateRequest implements Serializable {

    /**
     * id
     */
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
     * 标签列表
     */
    private List<String> tags;

    private static final long serialVersionUID = 1L;
}