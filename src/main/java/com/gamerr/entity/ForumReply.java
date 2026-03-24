package com.gamerr.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 论坛回复实体类
 */
@Data
@TableName("t_forum_reply")
public class ForumReply {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属帖子ID
     */
    private Long postId;

    /**
     * 回复者用户ID
     */
    private Long userId;

    /**
     * 回复者用户名（非数据库字段，用于显示）
     */
    @TableField(exist = false)
    private String username;

    /**
     * 回复者昵称（非数据库字段，用于显示）
     */
    @TableField(exist = false)
    private String nickname;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 回复状态：0-删除，1-正常
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
}
