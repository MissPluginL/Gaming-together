package com.gamerr.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 游戏实体类
 */
@Data
@TableName("t_game")
public class Game {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 游戏名称
     */
    private String name;

    /**
     * 游戏类型
     */
    private String category;

    /**
     * 游戏平台
     */
    private String platform;

    /**
     * 游戏封面图URL
     */
    private String coverImage;

    /**
     * 游戏描述
     */
    private String description;

    /**
     * 发行日期
     */
    private LocalDateTime releaseDate;

    /**
     * 开发商
     */
    private String developer;

    /**
     * 游戏价格
     */
    private BigDecimal price;

    /**
     * 预约人数
     */
    private Integer reservationCount;

    /**
     * 游戏状态：0-未上线，1-预约中，2-已上线
     */
    private Integer status;

    /**
     * 下载地址/获取渠道
     */
    private String downloadUrl;

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
