package com.gamerr.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户登录响应DTO
 */
@Data
public class LoginVO {

    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private Integer status;
    private String token;
    private LocalDateTime loginTime;

    /** ADMIN / USER，管理员用户名固定为 admin */
    private String role;
}
