package com.general.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiScope {

    PUBLIC(1, "公有"),
    PRIVATE(2, "私有"),
    ;

    @EnumValue
    private final int value;
    private final String desc;

    public static ApiScope valueOf(int value) {
        return Arrays.stream(values()).filter(t -> t.value == value).findFirst().orElse(null);
    }
}
