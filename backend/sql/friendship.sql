-- 好友关系表
CREATE TABLE IF NOT EXISTS `t_friendship` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关系ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `friend_id` BIGINT NOT NULL COMMENT '好友ID',
  `relation_type` TINYINT NOT NULL DEFAULT 0 COMMENT '关系类型：0-普通好友，1-单主，2-陪玩/代练',
  `remark` VARCHAR(50) DEFAULT NULL COMMENT '备注名',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '成为好友的时间',
  `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_friend_id` (`friend_id`),
  KEY `idx_relation_type` (`relation_type`),
  CONSTRAINT `fk_friendship_user` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_friendship_friend` FOREIGN KEY (`friend_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='好友关系表';
