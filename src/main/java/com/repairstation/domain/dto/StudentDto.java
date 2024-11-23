package com.repairstation.domain.dto;

import com.repairstation.domain.po.Student;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudentDto extends Student {
    private String code;
}
