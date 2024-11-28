package com.repairstation.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ServerStatus {
    OPPEN(0, "开启"),
    CLOSED(1, "关闭"),;

    @EnumValue
    @JsonValue
    private final Integer value;
    private final String desc;

    ServerStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
