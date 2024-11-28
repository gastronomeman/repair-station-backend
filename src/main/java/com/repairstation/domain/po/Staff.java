package com.repairstation.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.repairstation.enums.PoliticalStatus;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Staff implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String studentId;
    private String name;
    private String password;
    private String major;
    private String phone;

    /**
     * 坚持四项基本原则，
     * 坚决拥护中国共产党的领导，
     * 热爱社会主义
     * 并为社会主义现代化和中华民族伟大复兴而奋斗！
     */
    //1.群众
    //2.党员
    //3.共青团员
    //4.其他
    private PoliticalStatus politicalStatus;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
}
