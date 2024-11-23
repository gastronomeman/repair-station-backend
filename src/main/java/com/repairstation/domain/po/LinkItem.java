package com.repairstation.domain.po;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class LinkItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String url;
    private String photo;

}
