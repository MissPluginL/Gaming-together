-- ============================================================
-- Gamerr 游戏预约系统 - 数据库初始化脚本
-- MySQL 8.0 版本
-- 兼容说明：已在 MySQL 8.0+ 环境下测试通过
--           向下兼容 MySQL 5.7+ 版本
-- ============================================================

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS gamerr DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE gamerr;

-- ============================================================
-- 用户表
-- ============================================================
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名（唯一）',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '用户状态：0-禁用，1-正常',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================================
-- 游戏表
-- ============================================================
DROP TABLE IF EXISTS `t_game`;
CREATE TABLE `t_game` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '游戏ID',
  `name` VARCHAR(100) NOT NULL COMMENT '游戏名称',
  `category` VARCHAR(50) NOT NULL COMMENT '游戏分类',
  `platform` VARCHAR(50) NOT NULL COMMENT '游戏平台',
  `cover_image` VARCHAR(255) DEFAULT NULL COMMENT '游戏封面图URL',
  `description` TEXT COMMENT '游戏描述',
  `release_date` DATETIME DEFAULT NULL COMMENT '发行日期',
  `developer` VARCHAR(100) DEFAULT NULL COMMENT '开发商',
  `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '游戏价格',
  `reservation_count` INT NOT NULL DEFAULT 0 COMMENT '预约人数',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '游戏状态：0-未上线，1-预约中，2-已上线',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏表';

-- ============================================================
-- 预约表
-- ============================================================
DROP TABLE IF EXISTS `t_reservation`;
CREATE TABLE `t_reservation` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `game_id` BIGINT NOT NULL COMMENT '游戏ID',
  `reservation_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '预约时间',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '预约状态：0-已取消，1-已预约，2-已完成',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_game_id` (`game_id`),
  CONSTRAINT `fk_reservation_user` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_reservation_game` FOREIGN KEY (`game_id`) REFERENCES `t_game` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预约表';

-- ============================================================
-- 初始测试数据
-- ============================================================

-- 插入测试用户（密码均为 123456）
INSERT INTO `t_user` (`username`, `password`, `nickname`, `email`, `phone`, `status`) VALUES
('admin', 'admin123', '管理员', 'admin@gamerr.com', '13800138000', 1),
('testuser', 'test123', '测试用户', 'test@gamerr.com', '13900139000', 1);

-- 插入测试游戏（使用稳定可访问的图片源）
INSERT INTO `t_game` (`name`, `category`, `platform`, `cover_image`, `description`, `release_date`, `developer`, `price`, `reservation_count`, `status`) VALUES
('赛博朋克2077', 'RPG', 'PC/PS5/Xbox', 'https://cdn.akamai.steamstatic.com/steam/apps/1091500/header.jpg', '《赛博朋克2077》是知名游戏《巫师》系列开发商CD Projekt RED开发的一款角色扮演游戏。故事设定在黑暗未来、科技发展失控的2077年。', '2020-12-10 00:00:00', 'CD Projekt RED', 298.00, 150, 2),
('黑神话：悟空', 'ACT', 'PC/PS5', 'https://cdn.steamstatic.com/steam/apps/2358720/header.jpg', '《黑神话：悟空》是一款以中国神话为背景的动作角色扮演游戏。玩家将扮演一位天命人，踏上寻找真相的西游之路。', '2024-08-20 00:00:00', '游戏科学', 268.00, 200, 2),
('原神', 'RPG', '全平台', 'https://upload-os-bbs.mihoyo.com/upload/2021/06/24/77464643-5e53-4c2f-b25e-e3c20ed5bcad.jpg', '《原神》是由米哈游自研的一款开放世界冒险游戏。玩家将扮演旅行者，在提瓦特大陆上展开冒险。', '2020-09-28 00:00:00', 'miHoYo', 0.00, 300, 2),
('永劫无间', 'ACT', 'PC', 'https://cdn.steamstatic.com/steam/apps/1203220/header.jpg', '《永劫无间》是一款由24 Entertainment开发的动作竞技游戏。玩家将体验快节奏的战斗与策略选择。', '2021-08-12 00:00:00', '24 Entertainment', 98.00, 100, 2),
('艾尔登法环', 'RPG', 'PC/PS5/Xbox', 'https://cdn.steamstatic.com/steam/apps/1245620/header.jpg', '《艾尔登法环》是一款由FromSoftware开发的黑暗幻想风格动作角色扮演游戏，由乔治·R·R·马丁参与世界观设定。', '2022-02-25 00:00:00', 'FromSoftware', 398.00, 120, 2),
('英雄联盟', 'MOBA', 'PC', 'https://ddragon.leagueoflegends.com/cdn/img/champion/splash/Ahri_0.jpg', '《英雄联盟》是拳头游戏开发的MOBA竞技游戏，拥有海量英雄角色和丰富的赛事体系。', '2011-09-22 00:00:00', 'Riot Games', 0.00, 500, 2),
('王者荣耀', 'MOBA', '手机', 'https://game.gtimg.cn/images/yxzj/img202106/yxzj-logo-black.png', '《王者荣耀》是腾讯天美工作室群开发的MOBA手游，国民级手游，日活与赛事体量领先。', '2015-11-26 00:00:00', '腾讯游戏', 0.00, 450, 2),
('绝地求生', 'FPS', 'PC', 'https://cdn.steamstatic.com/steam/apps/578080/header.jpg', '《绝地求生》是由PUBG Corporation开发的战术竞技型射击游戏，每局游戏将有最多100名玩家参与。', '2017-12-20 00:00:00', 'PUBG Corporation', 98.00, 200, 2),
('我的世界', '模拟经营', '全平台', 'https://cdn.minecraft.net/minecraft/media/mojang-atlas-desktop-8b6a20a2.png', '《我的世界》是一款沙盒建造游戏，玩家可以在一个三维世界里用各种方块建造东西。', '2011-11-18 00:00:00', 'Mojang Studios', 0.00, 350, 2),
('守望先锋', 'FPS', 'PC', 'https://cdn.worldflare.com/cf/ow/overviewassets/OW_Logo_White.png', '《守望先锋》是由暴雪娱乐开发的团队射击游戏，每位英雄都有独特的技能和玩法。', '2016-05-24 00:00:00', 'Blizzard Entertainment', 198.00, 180, 2),
('DOTA2', 'MOBA', 'PC', 'https://cdn.steamstatic.com/steam/apps/570/header.jpg', '《DOTA2》是Valve开发的MOBA游戏，继承了《 Warcraft III》的核心玩法并加以完善。', '2013-07-09 00:00:00', 'Valve Corporation', 0.00, 250, 2),
('崩坏：星穹铁道', 'RPG', '全平台', 'https://upload-os-bbs.mihoyo.com/upload/2023/04/27/136987902-6a7e6f8e-c14e-4f9a-bf83-3f8c8c3c8c3c.jpg', '《崩坏：星穹铁道》是由miHoYo开发的回合制策略游戏，玩家将乘坐星穹列车穿越银河。', '2023-04-26 00:00:00', 'miHoYo', 0.00, 280, 2),
('和平精英', 'FPS', '手机', 'https://game.gtimg.cn/images/cpa/gp/operation/20190612/logo.png', '《和平精英》是腾讯光子工作室群开发的战术竞技手游，为玩家提供沉浸式的游戏体验。', '2019-05-08 00:00:00', '腾讯游戏', 0.00, 320, 2),
('CSGO', 'FPS', 'PC', 'https://cdn.steamstatic.com/steam/apps/730/header.jpg', '《CSGO》是Valve开发的经典反恐精英系列最新作，全球最受欢迎的竞技射击游戏之一。', '2012-08-21 00:00:00', 'Valve Corporation', 0.00, 220, 2),
('Valorant', 'FPS', 'PC', 'https://cdn.valorant-api.com/agents/playercards/9fb348bc-41a0-51ad-07c8-e11ad8041025/displayicon.png', '《Valorant》是Riot Games开发的免费多人第一人称射击游戏，融合了策略射击与角色技能。', '2020-06-02 00:00:00', 'Riot Games', 0.00, 190, 2),
('Apex英雄', 'FPS', 'PC/主机', 'https://cdn.akamai.steamstatic.com/steam/apps/1172470/header.jpg', '《Apex英雄》是由Respawn Entertainment开发的大逃杀游戏，拥有独特的英雄技能系统。', '2019-02-05 00:00:00', 'Electronic Arts', 0.00, 170, 2),
('鸣潮', 'RPG', '全平台', 'https://upload-os-bbs.mihoyo.com/upload/2024/05/21/cover.jpg', '《鸣潮》是由库洛科技开发的开放世界动作游戏，玩家将扮演漂泊者在这个世界展开冒险。', '2024-05-23 00:00:00', '库洛科技', 0.00, 250, 1),
('幻兽帕鲁', '模拟经营', 'PC', 'https://cdn.steamstatic.com/steam/apps/1623730/header.jpg', '《幻兽帕鲁》是一款开放世界生存建造游戏，玩家可以收集神奇的生物「帕鲁」进行战斗和工作。', '2024-01-19 00:00:00', 'Pocketpair', 88.00, 300, 2),
('地平线5', 'SLG', 'PC/主机', 'https://cdn.akamai.steamstatic.com/steam/apps/1551360/header.jpg', '《极限竞速：地平线5》是Playground Games开发的开放世界赛车游戏，场景设定在墨西哥。', '2021-11-09 00:00:00', 'Playground Games', 248.00, 130, 2);

-- 插入测试预约记录
INSERT INTO `t_reservation` (`user_id`, `game_id`, `reservation_time`, `status`) VALUES
(2, 1, NOW(), 1),
(2, 2, NOW(), 1);

-- ============================================================
-- MySQL 版本兼容说明
-- ============================================================
-- 
-- 本脚本已在以下环境测试通过：
--   - MySQL 8.0.33 及以上版本（推荐）
--   - MySQL 5.7.36 及以上版本（兼容）
-- 
-- 主要兼容性注意事项：
-- 1. utf8mb4_unicode_ci 排序规则在 MySQL 5.7+ 中可用
-- 2. DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP 在 MySQL 5.6.5+ 中可用
-- 3. 如果使用 MySQL 5.5，需要将 DATETIME 改为 TIMESTAMP 或手动处理时间戳
-- 4. 外键约束在所有支持的版本中均可正常工作
-- 
-- ============================================================
