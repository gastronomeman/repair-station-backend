package com.repairstation.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class RepairStationStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    private Long id;
    /**
     * 0.开启
     * 1.关闭
     */
    private Integer serverStatus;

    private String orderNotice;
    private String stopNotice;

    private String staffNotice;

    private String toolBox;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
