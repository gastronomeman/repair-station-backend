package com.repairstation.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.repairstation.enums.OrderIdentity;
import com.repairstation.enums.OrderStatus;
import com.repairstation.enums.OrderType;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Orders implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    /**
     * 0.老师
     * 1.学生
     */
    private OrderIdentity identity;
    private String studentId;
    private String building;
    private String dormitory;
    private String phone;
    /**
     * 1.软件类
     * 2.硬件类
     * 3.网络类
     * 4.手机类
     */
    private OrderType orderType;
    private String orderDescribe;
    /**
     * 1.待接单
     * 2.维修中
     * 3.已完成
     * 4.作废
     */
    private OrderStatus status;
    private Long staffId;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
    private LocalDateTime completionTime;

    //转单人
    private Long assignor;

}
