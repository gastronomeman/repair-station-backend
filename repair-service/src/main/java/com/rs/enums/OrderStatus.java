package com.rs.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum OrderStatus {
    WAIT(1, "待接单"),
    REPAIR(2, "维修中"),
    COMPLETE(3, "已完成"),
    CANCEL(4, "已作废");

    @EnumValue
    @JsonValue
    private final Integer value;
    private final String desc;

    OrderStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
