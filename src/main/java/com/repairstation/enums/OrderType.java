package com.repairstation.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum OrderType {
    SOFTWARE(1, "软件类"),
    HARDWARE(2, "硬件类"),
    NETWORK(3, "网络类"),
    PHONE(4, "手机类"),
    ;

    @EnumValue
    @JsonValue
    private final Integer value;
    private final String desc;

    OrderType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
