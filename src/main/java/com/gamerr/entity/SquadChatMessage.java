package com.gamerr.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 开黑房间聊天消息
 */
@Data
@TableName("t_squad_chat")
public class SquadChatMessage {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 所属房间ID */
    private Long roomId;

    /** 发送用户ID */
    private Long userId;

    /** 用户昵称（非数据库字段） */
    @TableField(exist = false)
    private String userNickname;

    /** 消息内容 */
    private String content;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;
}
