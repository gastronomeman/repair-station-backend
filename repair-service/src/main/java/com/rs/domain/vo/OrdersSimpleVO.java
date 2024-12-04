package com.rs.domain.vo;

import com.rs.enums.OrderIdentity;
import com.rs.enums.OrderStatus;
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
    private OrderIdentity identity;
    private String building;
    private String dormitory;

    private String orderDescribe;
    /**
     * 1.待接单
     * 2.维修中
     * 3.已完成
     */
    private OrderStatus status;

    private String staffName;
    private String staffPhone;

    private LocalDateTime createTime;
    private LocalDateTime completionTime;
}
