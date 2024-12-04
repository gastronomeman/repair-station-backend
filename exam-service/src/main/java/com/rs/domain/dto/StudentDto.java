package com.rs.domain.dto;

import com.rs.domain.po.Student;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudentDto extends Student {
    private String code;
}
