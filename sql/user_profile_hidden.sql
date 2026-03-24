-- 个人中心隐私：他人不可见时返回「该用户信息保密」
-- 在 Navicat / MySQL 客户端中选中 gamerr 数据库后执行本文件。
-- 若提示 Duplicate column name，说明列已存在，可忽略。

-- 1. 用户表：隐私开关
ALTER TABLE `t_user`
  ADD COLUMN `profile_hidden` TINYINT NOT NULL DEFAULT 0 COMMENT '0-他人可查看个人中心 1-不让他人查看' AFTER `avatar_audit_status`;

-- 2. 订单表：打手提交凭据 + 管理员审核
ALTER TABLE `t_service_order`
  ADD COLUMN `submitted` TINYINT NOT NULL DEFAULT 0 COMMENT '0-未提交凭据 1-已提交' AFTER `paid`,
  ADD COLUMN `proof_urls` TEXT NULL COMMENT '完成凭据URL列表，多个逗号分隔' AFTER `submitted`,
  ADD COLUMN `audit_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-待审核 1-通过 2-拒绝' AFTER `proof_urls`,
  ADD COLUMN `audit_reject_reason` VARCHAR(500) NULL COMMENT '管理员拒绝原因' AFTER `audit_status`;
