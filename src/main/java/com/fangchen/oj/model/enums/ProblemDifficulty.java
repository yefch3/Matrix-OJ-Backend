package com.fangchen.oj.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

@Getter
public enum ProblemDifficulty {
    EASY("Easy", 0),
    MEDIUM("Medium", 1),
    HARD("Hard", 2);

    private final String text;

    private final Integer value;

    ProblemDifficulty(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static ProblemDifficulty getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (ProblemDifficulty anEnum : ProblemDifficulty.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

}
