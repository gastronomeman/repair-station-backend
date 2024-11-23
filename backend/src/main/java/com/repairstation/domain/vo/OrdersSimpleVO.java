package com.repairstation.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrdersSimpleVO {

    private Long id;
    private String name;
    /**
     * 0.老师
     * 1.学生
     */
    private Integer identity;
    private String building;
    private String dormitory;

    private String orderDescribe;
    /**
     * 1.待接单
     * 2.维修中
     * 3.已完成
     */
    private Integer status;

    private String staffName;
    private String staffPhone;

    private LocalDateTime createTime;
    private LocalDateTime completionTime;
}
