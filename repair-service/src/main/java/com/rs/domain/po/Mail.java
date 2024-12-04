package com.rs.domain.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Mail implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String recipient ;//邮件接收人
    private String subject; //邮件主题（标题）
    private String content; //邮件内容
}
