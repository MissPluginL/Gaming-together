-- ============================================================
-- Gamerr 游戏预约系统 - 数据库更新脚本
-- 用于在已有数据库上添加更多游戏数据
-- ============================================================

USE gamerr;

-- 先删除现有的测试游戏数据（可选，如果想保留原有数据可以注释掉这部分）
-- DELETE FROM `t_game` WHERE `name` IN ('赛博朋克2077', '黑神话：悟空', '原神', '永劫无间', '艾尔登法环');

-- 添加更多游戏数据
INSERT INTO `t_game` (`name`, `category`, `platform`, `cover_image`, `description`, `release_date`, `developer`, `price`, `reservation_count`, `status`) VALUES
('英雄联盟', 'MOBA', 'PC', 'https://cdn-old.leagueoflegends.com/cdncdn/images/riot-games-share-a78f6b9.png', '《英雄联盟》是拳头游戏开发的MOBA竞技游戏，拥有海量英雄角色和丰富的赛事体系。', '2011-09-22 00:00:00', 'Riot Games', 0.00, 500, 2),
('王者荣耀', 'MOBA', '手机', 'https://game.gtimg.cn/images/yxzj/img202106/yxzj-logo-black.png', '《王者荣耀》是腾讯天美工作室群开发的MOBA手游，国民级手游，日活与赛事体量领先。', '2015-11-26 00:00:00', '腾讯游戏', 0.00, 450, 2),
('绝地求生', 'FPS', 'PC', 'https://cdn.steamstatic.com/steam/apps/578080/header.jpg', '《绝地求生》是由PUBG Corporation开发的战术竞技型射击游戏，每局游戏将有最多100名玩家参与。', '2017-12-20 00:00:00', 'PUBG Corporation', 98.00, 200, 2),
('我的世界', '模拟经营', '全平台', 'https://cdn.minecraft.net/minecraft/media/mojang-atlas-desktop-8b6a20a2.png', '《我的世界》是一款沙盒建造游戏，玩家可以在一个三维世界里用各种方块建造东西。', '2011-11-18 00:00:00', 'Mojang Studios', 0.00, 350, 2),
('守望先锋', 'FPS', 'PC', 'https://cdn.worldflare.com/cf/ow/overviewassets/OW_Logo_White.png', '《守望先锋》是由暴雪娱乐开发的团队射击游戏，每位英雄都有独特的技能和玩法。', '2016-05-24 00:00:00', 'Blizzard Entertainment', 198.00, 180, 2),
('DOTA2', 'MOBA', 'PC', 'https://cdn.steamstatic.com/steam/apps/570/header.jpg', '《DOTA2》是Valve开发的MOBA游戏，继承了《 Warcraft III》的核心玩法并加以完善。', '2013-07-09 00:00:00', 'Valve Corporation', 0.00, 250, 2),
('崩坏：星穹铁道', 'RPG', '全平台', 'https://upload-os-bbs.mihoyo.com/upload/2023/04/27/136987902-6a7e6f8e-c14e-4f9a-bf83-3f8c8c3c8c3c.jpg', '《崩坏：星穹铁道》是由miHoYo开发的回合制策略游戏，玩家将乘坐星穹列车穿越银河。', '2023-04-26 00:00:00', 'miHoYo', 0.00, 280, 2),
('和平精英', 'FPS', '手机', 'https://game.gtimg.cn/images/cpa/gp/operation/20190612/logo.png', '《和平精英》是腾讯光子工作室群开发的战术竞技手游，为玩家提供沉浸式的游戏体验。', '2019-05-08 00:00:00', '腾讯游戏', 0.00, 320, 2),
('CSGO', 'FPS', 'PC', 'https://cdn.steamstatic.com/steam/apps/730/header.jpg', '《CSGO》是Valve开发的经典反恐精英系列最新作，全球最受欢迎的竞技射击游戏之一。', '2012-08-21 00:00:00', 'Valve Corporation', 0.00, 220, 2),
('Valorant', 'FPS', 'PC', 'https://cdn.valorant-api.com/agents/playercards/9fb348bc-41a0-51ad-07c8-e11ad8041025/displayicon.png', '《Valorant》是Riot Games开发的免费多人第一人称射击游戏，融合了策略射击与角色技能。', '2020-06-02 00:00:00', 'Riot Games', 0.00, 190, 2),
('Apex英雄', 'FPS', 'PC/主机', 'https://cdn.cloudflare.dynatrace.com/apexlegends/icons/apex_legends_symbol.png', '《Apex英雄》是由Respawn Entertainment开发的大逃杀游戏，拥有独特的英雄技能系统。', '2019-02-05 00:00:00', 'Electronic Arts', 0.00, 170, 2),
('鸣潮', 'RPG', '全平台', 'https://upload-os-bbs.mihoyo.com/upload/2024/05/21/cover.jpg', '《鸣潮》是由库洛科技开发的开放世界动作游戏，玩家将扮演漂泊者在这个世界展开冒险。', '2024-05-23 00:00:00', '库洛科技', 0.00, 250, 1),
('幻兽帕鲁', '模拟经营', 'PC', 'https://cdn.steamstatic.com/steam/apps/1623730/header.jpg', '《幻兽帕鲁》是一款开放世界生存建造游戏，玩家可以收集神奇的生物「帕鲁」进行战斗和工作。', '2024-01-19 00:00:00', 'Pocketpair', 88.00, 300, 2),
('地平线5', 'SLG', 'PC/主机', 'https://cdn.akamai.steamstatic.com/steam/apps/1551360/header.jpg', '《极限竞速：地平线5》是Playground Games开发的开放世界赛车游戏，场景设定在墨西哥。', '2021-11-09 00:00:00', 'Playground Games', 248.00, 130, 2);

-- 更新现有游戏的封面图片（将无效的example.com图片替换为真实可用的图片）
UPDATE `t_game` SET `cover_image` = 'https://cdn.akamai.steamstatic.com/steam/apps/1091500/header.jpg' WHERE `name` = '赛博朋克2077' AND (`cover_image` IS NULL OR `cover_image` LIKE '%example.com%');
UPDATE `t_game` SET `cover_image` = 'https://cdn.steamstatic.com/steam/apps/2358720/header.jpg' WHERE `name` = '黑神话：悟空' AND (`cover_image` IS NULL OR `cover_image` LIKE '%example.com%');
UPDATE `t_game` SET `cover_image` = 'https://upload-os-bbs.mihoyo.com/upload/2021/06/24/77464643-5e53-4c2f-b25e-e3c20ed5bcad.jpg' WHERE `name` = '原神' AND (`cover_image` IS NULL OR `cover_image` LIKE '%example.com%');
UPDATE `t_game` SET `cover_image` = 'https://cdn.steamstatic.com/steam/apps/1203220/header.jpg' WHERE `name` = '永劫无间' AND (`cover_image` IS NULL OR `cover_image` LIKE '%example.com%');
UPDATE `t_game` SET `cover_image` = 'https://cdn.steamstatic.com/steam/apps/1245620/header.jpg' WHERE `name` = '艾尔登法环' AND (`cover_image` IS NULL OR `cover_image` LIKE '%example.com%');

-- 更新游戏分类为英文缩写（使前端分类匹配更准确）
UPDATE `t_game` SET `category` = 'RPG' WHERE `category` = '角色扮演';
UPDATE `t_game` SET `category` = 'ACT' WHERE `category` = '动作';
UPDATE `t_game` SET `category` = 'RPG' WHERE `category` = '冒险';

-- 验证结果
SELECT `id`, `name`, `category`, `platform`, `price`, `reservation_count`, `status` FROM `t_game` ORDER BY `id`;
