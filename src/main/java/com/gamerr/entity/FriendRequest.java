package com.gamerr.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 好友申请实体类
 */
@Data
@TableName("t_friend_request")
public class FriendRequest {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 申请人ID
     */
    private Long fromUserId;

    /**
     * 被申请人ID
     */
    private Long toUserId;

    /**
     * 申请状态：0-待处理，1-已同意，2-已拒绝
     */
    private Integer status;

    /**
     * 申请留言
     */
    private String message;

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
