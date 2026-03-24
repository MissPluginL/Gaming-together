-- Gamerr 需求表扩展：个人中心字段、订单、评价、封面图
-- 在 Navicat 中选中 gamerr 库后执行（若列已存在会报错，可忽略对应语句）

SET NAMES utf8mb4;

-- 用户：段位、技能标签、头像审核
ALTER TABLE t_user ADD COLUMN game_rank VARCHAR(100) NULL COMMENT '游戏段位/擅长' AFTER avatar;
ALTER TABLE t_user ADD COLUMN skill_tags VARCHAR(500) NULL COMMENT '技能标签' AFTER game_rank;
ALTER TABLE t_user ADD COLUMN avatar_audit_status INT NOT NULL DEFAULT 1 COMMENT '0待审1通过2拒绝' AFTER skill_tags;

-- 个人中心隐私（他人不可见时接口返回「该用户信息保密」）
ALTER TABLE t_user ADD COLUMN profile_hidden TINYINT NOT NULL DEFAULT 0 COMMENT '0-他人可查看个人中心 1-不让他人查看' AFTER avatar_audit_status;

-- 订单凭据审核（打手上传凭据 → 管理员审核通过后结算）
ALTER TABLE t_service_order ADD COLUMN submitted TINYINT NOT NULL DEFAULT 0 COMMENT '0-未提交凭据 1-已提交' AFTER paid;
ALTER TABLE t_service_order ADD COLUMN proof_urls TEXT NULL COMMENT '完成凭据图片URL，多个逗号分隔' AFTER submitted;
ALTER TABLE t_service_order ADD COLUMN audit_status TINYINT NOT NULL DEFAULT 0 COMMENT '0-待审核 1-通过 2-拒绝' AFTER proof_urls;
ALTER TABLE t_service_order ADD COLUMN audit_reject_reason VARCHAR(500) NULL COMMENT '管理员拒绝原因' AFTER audit_status;

-- 若上面列已存在，请改为：
-- UPDATE t_user SET avatar_audit_status = 1 WHERE avatar_audit_status IS NULL;

-- 订单
CREATE TABLE IF NOT EXISTS t_service_order (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  publisher_id BIGINT NOT NULL COMMENT '发单人',
  title VARCHAR(200) NOT NULL,
  game_name VARCHAR(100) NULL,
  description TEXT NULL,
  budget DECIMAL(10,2) DEFAULT 0,
  status INT NOT NULL DEFAULT 0 COMMENT '0待接单1进行中2已完成3已取消',
  taker_id BIGINT NULL,
  paid INT NOT NULL DEFAULT 0 COMMENT '0未支付1已支付(模拟)',
  deleted TINYINT NOT NULL DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_pub (publisher_id),
  INDEX idx_taker (taker_id),
  INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 评价
CREATE TABLE IF NOT EXISTS t_evaluation (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  order_id BIGINT NOT NULL,
  from_user_id BIGINT NOT NULL,
  to_user_id BIGINT NOT NULL,
  rating INT NOT NULL,
  comment VARCHAR(500) NULL,
  deleted TINYINT NOT NULL DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_order (order_id),
  INDEX idx_from (from_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 游戏封面：稳定占位图（避免外链失效）
UPDATE t_game SET cover_image = CONCAT('https://picsum.photos/seed/gamerr', id, '/400/225') WHERE deleted = 0;
