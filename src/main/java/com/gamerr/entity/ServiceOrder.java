package com.gamerr.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 陪玩/代练订单（需求表：发单、接单、取消）
 */
@Data
@TableName("t_service_order")
public class ServiceOrder {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 发单人 */
    private Long publisherId;

    private String title;

    private String gameName;

    private String description;

    private BigDecimal budget;

    /**
     * 0-待接单 1-进行中 2-已完成 3-已取消
     */
    private Integer status;

    /** 接单人 */
    private Long takerId;

    /** 模拟支付：0-未支付 1-已支付 */
    private Integer paid;

    /** 打手是否已提交完成凭据：0-未提交 1-已提交 */
    private Integer submitted;

    /** 完成凭据URL列表（多张图片逗号分隔） */
    private String proofUrls;

    /** 管理员审核状态：0-待审核 1-通过 2-拒绝 */
    private Integer auditStatus;

    /** 管理员拒绝原因 */
    private String auditRejectReason;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
