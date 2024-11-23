package com.repairstation.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class StaffSimpleVO implements Serializable {
    private Long id;
    private String name;
    private String studentId;
    private String major;
    private Integer orderCount;
}
