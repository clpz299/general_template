package com.general.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum OrganizationType {

    COMPANY(1, "公司"),
    DEPARTMENT(2, "部门"),
    ;

    @EnumValue
    private final int value;
    private final String desc;

    public static OrganizationType valueOf(int value) {
        return Arrays.stream(values()).filter(t -> t.value == value).findFirst().orElse(null);
    }
}
