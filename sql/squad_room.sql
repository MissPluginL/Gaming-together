-- =====================================================
-- 开黑房间表 SQL 脚本
-- 请在 MySQL 数据库中执行此脚本
-- =====================================================

-- 创建开黑房间表
CREATE TABLE IF NOT EXISTS `t_squad_room` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `title` VARCHAR(100) NOT NULL COMMENT '房间标题',
    `description` TEXT COMMENT '房间简介/描述',
    `game_id` BIGINT COMMENT '关联游戏ID',
    `room_number` VARCHAR(50) COMMENT '游戏房间号',
    `room_password` VARCHAR(50) COMMENT '房间密码',
    `max_players` INT NOT NULL DEFAULT 5 COMMENT '最大人数限制',
    `current_players` INT NOT NULL DEFAULT 1 COMMENT '当前人数',
    `owner_id` BIGINT NOT NULL COMMENT '房主用户ID',
    `status` INT NOT NULL DEFAULT 1 COMMENT '房间状态：0-已关闭，1-开放中',
    `room_type` INT NOT NULL DEFAULT 1 COMMENT '房间类型：1-匹配房间，2-语音开黑，3-比赛房间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` INT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
    PRIMARY KEY (`id`),
    INDEX `idx_game_id` (`game_id`),
    INDEX `idx_owner_id` (`owner_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='开黑房间表';

-- =====================================================
-- 可选：添加示例数据
-- =====================================================

-- INSERT INTO `t_squad_room` (`title`, `description`, `game_id`, `room_number`, `max_players`, `current_players`, `owner_id`, `status`, `room_type`)
-- VALUES ('王者荣耀五排缺一', '钻石段位，求一个打野队友', 1, 'QQ区-战队赛-8888', 5, 2, 1, 1, 1);

-- =====================================================
-- 开黑房间成员表 SQL 脚本
-- =====================================================

-- 创建开黑房间成员表
CREATE TABLE IF NOT EXISTS `t_squad_room_member` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `room_id` BIGINT NOT NULL COMMENT '房间ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `join_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    `deleted` INT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
    PRIMARY KEY (`id`),
    INDEX `idx_room_id` (`room_id`),
    INDEX `idx_user_id` (`user_id`),
    UNIQUE INDEX `idx_room_user` (`room_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='开黑房间成员表';
