package com.repairstation.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum PoliticalStatus {
    CITIZEN(1, "群众"),
    CPC(2, "共产党员"),
    CCYL(3, "共青团员"),
    OTHER(4, "其他"),
    ;

    @EnumValue
    @JsonValue
    private final Integer value;
    private final String desc;

    PoliticalStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
