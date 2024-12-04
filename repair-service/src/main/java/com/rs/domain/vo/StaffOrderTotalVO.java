package com.rs.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class StaffOrderTotalVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer orderTotal;
    private Integer weekOrderTotal;
}
