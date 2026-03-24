-- 若 proof_urls 仍为 VARCHAR 或需存较长 URL 列表，可执行本脚本（已 LONGTEXT 可忽略报错）
SET NAMES utf8mb4;
ALTER TABLE t_service_order
  MODIFY COLUMN proof_urls LONGTEXT NULL COMMENT '完成凭据图片URL，多个逗号分隔';
