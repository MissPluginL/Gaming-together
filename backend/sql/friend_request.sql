-- 好友申请表
CREATE TABLE IF NOT EXISTS `t_friend_request` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '申请ID',
  `from_user_id` BIGINT NOT NULL COMMENT '申请人ID',
  `to_user_id` BIGINT NOT NULL COMMENT '被申请人ID',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '申请状态：0-待处理，1-已同意，2-已拒绝',
  `message` VARCHAR(255) DEFAULT NULL COMMENT '申请留言',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_from_user_id` (`from_user_id`),
  KEY `idx_to_user_id` (`to_user_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_request_from_user` FOREIGN KEY (`from_user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_request_to_user` FOREIGN KEY (`to_user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='好友申请表';
