package com.gamerr.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单完成后的互评
 */
@Data
@TableName("t_evaluation")
public class Evaluation {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private Long fromUserId;

    private Long toUserId;

    private Integer rating;

    private String comment;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;
}
