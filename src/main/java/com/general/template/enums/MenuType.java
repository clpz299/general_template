package com.general.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MenuType {

    MENU(1, "菜单"),
    FUNCTION(2, "功能"),
    ;

    @EnumValue
    private final Integer value;
    private final String desc;
}

