package com.repairstation.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class OrdersWithStaffNameDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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
    private Long staffId;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;

    private String staffName;
}
