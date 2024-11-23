package com.repairstation.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FileVO implements Serializable {
    private String name;
    private LocalDateTime date;
}
