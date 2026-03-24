package com.gamerr.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 找回密码（演示：验证码固定 123456）
 */
@Data
public class ResetPasswordDTO {

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "验证码不能为空")
    private String verifyCode;

    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}
