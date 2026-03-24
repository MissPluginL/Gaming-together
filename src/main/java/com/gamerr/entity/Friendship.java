package com.gamerr.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 好友关系实体类
 */
@Data
@TableName("t_friendship")
public class Friendship {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 好友ID
     */
    private Long friendId;

    /**
     * 好友关系类型：0-普通好友，1-单主（对方是我的单主），2-陪玩/代练（对方是我的陪玩打手）
     */
    private Integer relationType;

    /**
     * 备注名
     */
    private String remark;

    /**
     * 创建时间（成为好友的时间）
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 逻辑删除标记
     */
    @TableLogic
    private Integer deleted;
}
