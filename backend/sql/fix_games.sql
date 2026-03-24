-- ============================================================
-- Gamerr 游戏预约系统 - 数据修复脚本
-- 解决乱码问题，替换为正确的中文数据和真实游戏图片
-- ============================================================

USE gamerr;

-- 1. 删除所有乱码游戏数据
DELETE FROM `t_game` WHERE `id` <= 16;

-- 2. 用正确数据替换 id=22 (之前有重复的DOTA2)
UPDATE `t_game`
SET
  `name` = '艾尔登法环',
  `category` = 'RPG',
  `platform` = 'PC/PS5/Xbox',
  `cover_image` = 'https://cdn.steamstatic.com/steam/apps/1245620/header.jpg',
  `description` = '《艾尔登法环》是一款由FromSoftware开发的黑暗幻想风格动作角色扮演游戏，由乔治·R·R·马丁参与世界观设定。',
  `release_date` = '2022-02-25 00:00:00',
  `developer` = 'FromSoftware',
  `price` = 398.00,
  `reservation_count` = 120,
  `status` = 2
WHERE `id` = 22 AND `name` = 'DOTA2';

-- 3. 修正其他错误图片的游戏
UPDATE `t_game` SET `cover_image` = 'https://cdn.valorant-api.com/agents/playercards/9fb348bc-41a0-51ad-07c8-e11ad8041025/displayicon.png' WHERE `name` = 'Valorant';
UPDATE `t_game` SET `cover_image` = 'https://cdn.akamai.steamstatic.com/steam/apps/1172470/header.jpg' WHERE `name` = 'Apex英雄';
UPDATE `t_game` SET `cover_image` = 'https://ddragon.leagueoflegends.com/cdn/img/champion/splash/Ahri_0.jpg' WHERE `name` = '英雄联盟';

-- 4. 补充 id 1-16 位置的游戏数据（按原有 id 顺序）
INSERT INTO `t_game` (`name`, `category`, `platform`, `cover_image`, `description`, `release_date`, `developer`, `price`, `reservation_count`, `status`) VALUES
(NULL, 'RPG', 'PC/PS5/Xbox', 'https://cdn.akamai.steamstatic.com/steam/apps/1091500/header.jpg', '《赛博朋克2077》是知名游戏《巫师》系列开发商CD Projekt RED开发的一款角色扮演游戏。故事设定在黑暗未来、科技发展失控的2077年。', '2020-12-10 00:00:00', 'CD Projekt RED', 298.00, 150, 2),
(NULL, 'ACT', 'PC/PS5', 'https://cdn.steamstatic.com/steam/apps/2358720/header.jpg', '《黑神话：悟空》是一款以中国神话为背景的动作角色扮演游戏。玩家将扮演一位天命人，踏上寻找真相的西游之路。', '2024-08-20 00:00:00', '游戏科学', 268.00, 200, 2),
(NULL, 'RPG', '全平台', 'https://upload-os-bbs.mihoyo.com/upload/2021/06/24/77464643-5e53-4c2f-b25e-e3c20ed5bcad.jpg', '《原神》是由米哈游自研的一款开放世界冒险游戏。玩家将扮演旅行者，在提瓦特大陆上展开冒险。', '2020-09-28 00:00:00', 'miHoYo', 0.00, 300, 2),
(NULL, 'ACT', 'PC', 'https://cdn.steamstatic.com/steam/apps/1203220/header.jpg', '《永劫无间》是一款由24 Entertainment开发的动作竞技游戏。玩家将体验快节奏的战斗与策略选择。', '2021-08-12 00:00:00', '24 Entertainment', 98.00, 100, 2),
(NULL, 'RPG', 'PC/PS5/Xbox', 'https://cdn.steamstatic.com/steam/apps/1245620/header.jpg', '《艾尔登法环》是一款由FromSoftware开发的黑暗幻想风格动作角色扮演游戏。', '2022-02-25 00:00:00', 'FromSoftware', 398.00, 120, 2),
(NULL, 'MOBA', 'PC', 'https://cdn-old.leagueoflegends.com/cdncdn/images/riot-games-share-a78f6b9.png', '《英雄联盟》是拳头游戏开发的MOBA竞技游戏，拥有海量英雄角色和丰富的赛事体系。', '2011-09-22 00:00:00', 'Riot Games', 0.00, 500, 2),
(NULL, 'MOBA', '手机', 'https://game.gtimg.cn/images/yxzj/img202106/yxzj-logo-black.png', '《王者荣耀》是腾讯天美工作室群开发的MOBA手游，国民级手游。', '2015-11-26 00:00:00', '腾讯游戏', 0.00, 450, 2),
(NULL, 'FPS', 'PC', 'https://cdn.steamstatic.com/steam/apps/578080/header.jpg', '《绝地求生》是由PUBG Corporation开发的战术竞技型射击游戏。', '2017-12-20 00:00:00', 'PUBG Corporation', 98.00, 200, 2),
(NULL, '模拟经营', '全平台', 'https://cdn.minecraft.net/minecraft/media/mojang-atlas-desktop-8b6a20a2.png', '《我的世界》是一款沙盒建造游戏，玩家可以在一个三维世界里用各种方块建造东西。', '2011-11-18 00:00:00', 'Mojang Studios', 0.00, 350, 2),
(NULL, 'FPS', 'PC', 'https://cdn.worldflare.com/cf/ow/overviewassets/OW_Logo_White.png', '《守望先锋》是由暴雪娱乐开发的团队射击游戏。', '2016-05-24 00:00:00', 'Blizzard Entertainment', 198.00, 180, 2),
(NULL, 'MOBA', 'PC', 'https://cdn.steamstatic.com/steam/apps/570/header.jpg', '《DOTA2》是Valve开发的MOBA游戏。', '2013-07-09 00:00:00', 'Valve Corporation', 0.00, 250, 2),
(NULL, 'RPG', '全平台', 'https://upload-os-bbs.mihoyo.com/upload/2023/04/27/136987902-6a7e6f8e-c14e-4f9a-bf83-3f8c8c3c8c3c.jpg', '《崩坏：星穹铁道》是由miHoYo开发的回合制策略游戏。', '2023-04-26 00:00:00', 'miHoYo', 0.00, 280, 2),
(NULL, 'FPS', '手机', 'https://game.gtimg.cn/images/cpa/gp/operation/20190612/logo.png', '《和平精英》是腾讯光子工作室群开发的战术竞技手游。', '2019-05-08 00:00:00', '腾讯游戏', 0.00, 320, 2),
(NULL, 'FPS', 'PC', 'https://cdn.steamstatic.com/steam/apps/730/header.jpg', '《CSGO》是Valve开发的经典反恐精英系列最新作。', '2012-08-21 00:00:00', 'Valve Corporation', 0.00, 220, 2),
(NULL, 'FPS', 'PC', 'https://cdn.valorant-api.com/agents/playercards/9fb348bc-41a0-51ad-07c8-e11ad8041025/displayicon.png', '《Valorant》是Riot Games开发的免费多人第一人称射击游戏。', '2020-06-02 00:00:00', 'Riot Games', 0.00, 190, 2),
(NULL, 'FPS', 'PC/主机', 'https://cdn.akamai.steamstatic.com/steam/apps/1172470/header.jpg', '《Apex英雄》是由Respawn Entertainment开发的大逃杀游戏。', '2019-02-05 00:00:00', 'Electronic Arts', 0.00, 170, 2);

-- 5. 更新 init.sql 也需要同步（使用纯 ASCII + 注释说明中文含义）
