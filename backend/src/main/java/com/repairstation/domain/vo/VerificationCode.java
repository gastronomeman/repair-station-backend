package com.repairstation.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class VerificationCode implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String code;
    private String time;
}
