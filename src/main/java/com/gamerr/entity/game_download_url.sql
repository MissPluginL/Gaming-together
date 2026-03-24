-- 给游戏表添加下载地址字段
ALTER TABLE t_game ADD COLUMN IF NOT EXISTS download_url VARCHAR(500) DEFAULT NULL COMMENT '下载地址/获取渠道' AFTER status;
