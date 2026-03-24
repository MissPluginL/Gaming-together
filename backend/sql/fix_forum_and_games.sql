-- ============================================================
-- Gamerr 游戏预约系统 - 数据清理与测试数据脚本
-- 清理问号占位图，补充真实测试数据
-- ============================================================

USE gamerr;

-- ============================================================
-- 1. 清理无效的游戏封面 URL
-- 把 example.com、placeholder、问号等无效图片清空，让 GameCoverService 自动补上正确封面
-- ============================================================
UPDATE t_game
SET cover_image = NULL
WHERE cover_image IS NOT NULL
  AND (
    cover_image LIKE '%example.com%'
    OR cover_image LIKE '%placeholder%'
    OR cover_image LIKE '%???%'
    OR cover_image LIKE '%question%'
    OR cover_image LIKE '%unknown%'
    OR cover_image = ''
  );

-- 强制所有游戏重新计算封面（设为 NULL 让 GameCoverService 处理）
UPDATE t_game SET cover_image = NULL WHERE deleted = 0;

-- ============================================================
-- 2. 清理旧论坛测试数据
-- ============================================================
DELETE FROM t_forum_reply WHERE deleted = 0;
DELETE FROM t_forum_post WHERE deleted = 0;

-- 重置自增 ID
ALTER TABLE t_forum_reply AUTO_INCREMENT = 1;
ALTER TABLE t_forum_post AUTO_INCREMENT = 1;

-- ============================================================
-- 3. 补充论坛测试帖子
-- 使用 admin 用户 (id=1) 和 testuser (id=2) 发布真实内容
-- ============================================================

-- 帖子1：游戏攻略讨论
INSERT INTO t_forum_post (title, content, user_id, game_id, status, create_time, update_time, deleted) VALUES
('【攻略分享】艾尔登法环新手入门指南', '大家好，今天给大家分享一下艾尔登法环的新手入门技巧。\n\n1. 开局职业选择：推荐选择武士或流浪骑士，容错率高\n2. 优先探索：不要急着推主线，先把宁姆格福探索完\n3. 骨灰系统：早点刷魂解锁流浪民族的骨灰，对新手很友好\n4. 锻造石：前期多收集，打出好的武器升级\n\n希望对新手玩家有帮助，欢迎补充！', 1, 1, 1, NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 5 DAY, 0);

-- 帖子2：赛事讨论
INSERT INTO t_forum_post (title, content, user_id, game_id, status, create_time, update_time, deleted) VALUES
('英雄联盟S15世界赛观赛指南', '今年的英雄联盟世界赛又要开始了，给大家整理一下观赛指南：\n\n赛程安排：10月开始，淘汰赛在11月\n参赛队伍：LPL、LCK、LEC等各赛区队伍\n重点关注：BLG、JDG的状态，Chovy的发挥\n\n大家觉得今年谁能夺冠？', 2, 6, 1, NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 3 DAY, 0);

-- 帖子3：组队招募
INSERT INTO t_forum_post (title, content, user_id, game_id, status, create_time, update_time, deleted) VALUES
('永劫无间三排上分找队友', '本人钻石段位，主玩胡桃和宁红夜，找两个稳定队友一起上分。\n\n要求：\n- 段位钻石以上\n- 晚上8点后有时间\n- 语音沟通\n- 不压力队友\n\n有兴趣的留言或私信~', 1, 4, 1, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY, 0);

-- 帖子4：游戏评测
INSERT INTO t_forum_post (title, content, user_id, game_id, status, create_time, update_time, deleted) VALUES
('黑神话悟空一周目通关感受', '终于一周目通关了，来说说感受：\n\n画面：10分，国产天花板，每一帧都可以当壁纸\n剧情：8分，有些地方有点赶，但总体很震撼\n战斗：9分，打击感很棒，BOSS战很有挑战性\n\n缺点：空气墙有点多，地图引导不够清晰\n\n总体来说是一款值得所有玩家体验的作品！', 2, 2, 1, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY, 0);

-- 帖子5：综合讨论（不关联游戏）
INSERT INTO t_forum_post (title, content, user_id, game_id, status, create_time, update_time, deleted) VALUES
('大家平时都玩什么游戏？', '潜水很久了，第一次发帖。\n\n我是从主机时代就开始玩游戏的，现在主要玩PC和主机。\n\n最近在玩：黑神话悟空、永劫无间\n经典老游戏：巫师3、老头环\n\n大家有什么推荐的游戏吗？最近有点游戏荒了~', 1, NULL, 1, NOW() - INTERVAL 6 HOUR, NOW() - INTERVAL 6 HOUR, 0);

-- 帖子6：游戏建议
INSERT INTO t_forum_post (title, content, user_id, game_id, status, create_time, update_time, deleted) VALUES
('希望增加游戏评价和评分功能', '建议增加一个玩家评分和评价功能，类似豆瓣那样。\n\n可以：\n- 让玩家对游戏打分（1-5星）\n- 写游戏评测\n- 看到游戏的综合评分\n\n这样选游戏的时候有个参考，感觉会很实用！', 2, NULL, 1, NOW() - INTERVAL 12 HOUR, NOW() - INTERVAL 12 HOUR, 0);

-- ============================================================
-- 4. 补充论坛回复
-- ============================================================

-- 帖子1的回复
INSERT INTO t_forum_reply (post_id, user_id, content, status, create_time, update_time, deleted) VALUES
(1, 2, '写得很好！补充一点：前期多刷魂把赐福点满，骑马跑图很重要。', 1, NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 4 DAY, 0),
(1, 1, '谢谢补充！我觉得碎片化信息收集也很重要，能加不少属性。', 1, NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 4 DAY, 0),
(1, 2, '对的！我就是漏了很多东西导致后面打BOSS很痛苦。', 1, NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 4 DAY, 0);

-- 帖子2的回复
INSERT INTO t_forum_reply (post_id, user_id, content, status, create_time, update_time, deleted) VALUES
(2, 1, '今年LCK状态很好，GenG和T1都很有希望，LPL压力很大啊。', 1, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY, 0),
(2, 2, '我觉得BLG还是很有竞争力的，全华班加油！', 1, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY, 0);

-- 帖子3的回复
INSERT INTO t_forum_reply (post_id, user_id, content, status, create_time, update_time, deleted) VALUES
(3, 2, '我有兴趣！我主要玩天海，也可以打输出，加个好友？', 1, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY, 0);

-- 帖子4的回复
INSERT INTO t_forum_reply (post_id, user_id, content, status, create_time, update_time, deleted) VALUES
(4, 1, '空气墙确实有点难受，不过瑕不掩瑜，年度最佳游戏预定！', 1, NOW() - INTERVAL 20 HOUR, NOW() - INTERVAL 20 HOUR, 0),
(4, 2, '同意，二郎神那场BOSS战打了我一下午，但是过的那一刻太爽了！', 1, NOW() - INTERVAL 18 HOUR, NOW() - INTERVAL 18 HOUR, 0);

-- 帖子5的回复
INSERT INTO t_forum_reply (post_id, user_id, content, status, create_time, update_time, deleted) VALUES
(5, 2, '最近在玩幻兽帕鲁，缝合得很成功，肝但是好玩！', 1, NOW() - INTERVAL 4 HOUR, NOW() - INTERVAL 4 HOUR, 0),
(5, 1, '推荐试试地平线5，画面和开车手感都很棒。', 1, NOW() - INTERVAL 3 HOUR, NOW() - INTERVAL 3 HOUR, 0);

-- 帖子6的回复
INSERT INTO t_forum_reply (post_id, user_id, content, status, create_time, update_time, deleted) VALUES
(6, 1, '好建议！感觉这个功能挺实用的，希望能实现。', 1, NOW() - INTERVAL 10 HOUR, NOW() - INTERVAL 10 HOUR, 0);

-- ============================================================
-- 5. 验证结果
-- ============================================================
SELECT '=== 游戏列表 ===' AS '';
SELECT id, name, category, cover_image, status FROM t_game WHERE deleted = 0 ORDER BY id;

SELECT '=== 论坛帖子 ===' AS '';
SELECT fp.id, fp.title, u.username, g.name AS game_name, fp.create_time,
       (SELECT COUNT(*) FROM t_forum_reply WHERE post_id = fp.id AND deleted = 0) AS reply_count
FROM t_forum_post fp
LEFT JOIN t_user u ON fp.user_id = u.id
LEFT JOIN t_game g ON fp.game_id = g.id
WHERE fp.deleted = 0
ORDER BY fp.id;
