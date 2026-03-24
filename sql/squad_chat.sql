-- =====================================================
-- 开黑房间聊天消息表
-- 请在 MySQL 数据库中执行此脚本
-- =====================================================
CREATE TABLE IF NOT EXISTS `t_squad_chat` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `room_id` BIGINT NOT NULL COMMENT '所属房间ID',
    `user_id` BIGINT NOT NULL COMMENT '发送用户ID',
    `content` TEXT NOT NULL COMMENT '消息内容',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    `deleted` INT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
    PRIMARY KEY (`id`),
    INDEX `idx_room_id` (`room_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='开黑房间聊天消息表';
