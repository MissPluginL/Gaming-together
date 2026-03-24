package com.gamerr.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 开黑房间实体类
 */
@Data
@TableName("t_squad_room")
public class SquadRoom {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 房间名称/标题
     */
    private String title;

    /**
     * 房间简介/描述
     */
    private String description;

    /**
     * 游戏ID
     */
    private Long gameId;

    /**
     * 游戏名称（非数据库字段，用于显示）
     */
    @TableField(exist = false)
    private String gameName;

    /**
     * 游戏房间号（如游戏房间ID、房间码等）
     */
    private String roomNumber;

    /**
     * 房间密码（可选）
     */
    private String roomPassword;

    /**
     * 最大人数限制
     */
    private Integer maxPlayers;

    /**
     * 当前人数
     */
    private Integer currentPlayers;

    /**
     * 房主用户ID
     */
    private Long ownerId;

    /**
     * 房主用户名（非数据库字段，用于显示）
     */
    @TableField(exist = false)
    private String ownerName;

    /**
     * 房主昵称（非数据库字段，用于显示）
     */
    @TableField(exist = false)
    private String ownerNickname;

    /**
     * 房间状态：0-已关闭，1-开放中
     */
    private Integer status;

    /**
     * 房间类型：1-匹配房间，2-语音开黑，3-比赛房间
     */
    private Integer roomType;

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
}
