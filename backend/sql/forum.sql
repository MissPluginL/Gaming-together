-- 论坛帖子表
CREATE TABLE IF NOT EXISTS `t_forum_post` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
  `title` VARCHAR(200) NOT NULL COMMENT '帖子标题',
  `content` TEXT NOT NULL COMMENT '帖子内容',
  `user_id` BIGINT NOT NULL COMMENT '发布者用户ID',
  `game_id` BIGINT DEFAULT NULL COMMENT '关联游戏ID',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '帖子状态：0-删除，1-正常',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_game_id` (`game_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_forum_post_user` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_forum_post_game` FOREIGN KEY (`game_id`) REFERENCES `t_game` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='论坛帖子表';

-- 论坛回复表
CREATE TABLE IF NOT EXISTS `t_forum_reply` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '回复ID',
  `post_id` BIGINT NOT NULL COMMENT '所属帖子ID',
  `user_id` BIGINT NOT NULL COMMENT '回复者用户ID',
  `content` TEXT NOT NULL COMMENT '回复内容',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '回复状态：0-删除，1-正常',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_forum_reply_post` FOREIGN KEY (`post_id`) REFERENCES `t_forum_post` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_forum_reply_user` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='论坛回复表';
