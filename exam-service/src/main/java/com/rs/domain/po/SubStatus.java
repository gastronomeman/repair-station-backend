package com.rs.domain.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class SubStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    Long id;
    String notice;
    Integer number;
    Integer isOpen;
    Integer pass;
}
