package com.gamerr.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 论坛帖子实体类
 */
@Data
@TableName("t_forum_post")
public class ForumPost {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 发布者用户ID
     */
    private Long userId;

    /**
     * 发布者用户名（非数据库字段，用于显示）
     */
    @TableField(exist = false)
    private String username;

    /**
     * 发布者昵称（非数据库字段，用于显示）
     */
    @TableField(exist = false)
    private String nickname;

    /**
     * 关联游戏ID（可选）
     */
    private Long gameId;

    /**
     * 游戏名称（非数据库字段，用于显示）
     */
    @TableField(exist = false)
    private String gameName;

    /**
     * 回复数量（非数据库字段）
     */
    @TableField(exist = false)
    private Integer replyCount;

    /**
     * 最后回复时间（非数据库字段）
     */
    @TableField(exist = false)
    private LocalDateTime lastReplyTime;

    /**
     * 帖子状态：0-删除，1-正常
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
