package com.gamerr.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@TableName("t_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名（唯一）
     */
    private String username;

    /**
     * 密码（加密存储）
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 游戏段位 / 擅长位置描述
     */
    private String gameRank;

    /**
     * 技能标签（逗号分隔）
     */
    private String skillTags;

    /**
     * 头像审核：0-待审核，1-通过，2-拒绝
     */
    private Integer avatarAuditStatus;

    /**
     * 个人中心隐私：0-他人可查看，1-不让他人查看个人中心信息
     */
    private Integer profileHidden;

    /**
     * 用户状态：0-禁用，1-正常
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标记
     */
    @TableLogic
    private Integer deleted;

    /** 非数据库字段：ADMIN / USER */
    @TableField(exist = false)
    private String role;
}
