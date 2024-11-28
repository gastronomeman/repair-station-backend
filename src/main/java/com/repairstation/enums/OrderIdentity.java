package com.repairstation.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum OrderIdentity {

    TEACHER(0, "教职工"),
    STUDENT(1, "学生"),
    ;

    @EnumValue
    @JsonValue
    private final Integer value;
    private final String desc;

    OrderIdentity(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
