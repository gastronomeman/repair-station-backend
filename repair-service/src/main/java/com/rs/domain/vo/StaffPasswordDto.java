package com.rs.domain.vo;

import lombok.Data;

@Data
public class StaffPasswordDto {
    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirm;
}
