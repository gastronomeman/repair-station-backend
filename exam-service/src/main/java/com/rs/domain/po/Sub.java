package com.rs.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Sub implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String topic;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private Integer result;

    private String type;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
