-- 若「我发的单 / 接单大厅」列表为空但数据库里有订单，多半是缺少下列列导致接口 SQL 报错。
-- 在 gamerr 库执行（列已存在会报错，可忽略对应语句）

SET NAMES utf8mb4;

ALTER TABLE t_service_order ADD COLUMN submitted TINYINT NOT NULL DEFAULT 0 COMMENT '0-未提交凭据 1-已提交' AFTER paid;
ALTER TABLE t_service_order ADD COLUMN proof_urls TEXT NULL COMMENT '完成凭据图片URL，多个逗号分隔' AFTER submitted;
ALTER TABLE t_service_order ADD COLUMN audit_status TINYINT NOT NULL DEFAULT 0 COMMENT '0-待审核 1-通过 2-拒绝' AFTER proof_urls;
ALTER TABLE t_service_order ADD COLUMN audit_reject_reason VARCHAR(500) NULL COMMENT '管理员拒绝原因' AFTER audit_status;
