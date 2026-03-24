package com.gamerr.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 开黑房间成员实体类
 */
@Data
@TableName("t_squad_room_member")
public class SquadRoomMember {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 房间ID
     */
    private Long roomId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户昵称（非数据库字段，用于显示）
     */
    @TableField(exist = false)
    private String userNickname;

    /**
     * 加入时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime joinTime;

    /**
     * 注意：不要在此实体中声明 {@code deleted} 字段。
     * application.yml 中配置了全局 logic-delete-field: deleted，
     * 若本表也有 deleted 列，会导致 Mapper.delete 变成逻辑删除，唯一索引 (room_id,user_id) 在再次加入时冲突。
     * 成员进出房使用物理删除；若需在 SQL 中过滤“有效成员”，在 QueryWrapper 里写 .eq("deleted", 0)。
     */
}
