package com.gamerr.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 游戏封面图服务
 *
 * 所有游戏封面已下载到本地 resources/static/images/covers/ 目录，
 * 通过 /images/covers/{文件名} 访问，完全不依赖外部 CDN。
 *
 * IGDB 预留接口：images.igdb.com 在国内无法访问，
 * 如需启用 IGDB 需配置 IGDB_CLIENT_ID + IGDB_CLIENT_SECRET。
 */
@Service
public class GameCoverService {

    private static final String LOCAL_COVER_BASE = "/api/images/covers";

    // Steam 图片映射：游戏名 -> 本地文件名
    private final Map<String, String> GAME_COVER_FILES = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
        // Steam 游戏封面（steamcdn-a.akamaihd.net 国内可访问，文件已缓存本地备用）
        GAME_COVER_FILES.put("赛博朋克2077", "cyberpunk2077.jpg");
        GAME_COVER_FILES.put("黑神话：悟空", "wukong.jpg");
        GAME_COVER_FILES.put("原神", "genshin.jpg");
        GAME_COVER_FILES.put("永劫无间", "ninja.jpg");
        GAME_COVER_FILES.put("艾尔登法环", "eldenring.jpg");
        GAME_COVER_FILES.put("英雄联盟", "lol.jpg");
        GAME_COVER_FILES.put("王者荣耀", "wangzhe.jpg");   // 腾讯，无 Steam
        GAME_COVER_FILES.put("绝地求生", "pubg.jpg");
        GAME_COVER_FILES.put("我的世界", "minecraft.jpg");
        GAME_COVER_FILES.put("守望先锋", "overwatch.jpg");
        GAME_COVER_FILES.put("守望先锋归来", "overwatch.jpg"); // OW2 同封面
        GAME_COVER_FILES.put("DOTA2", "dota2.jpg");
        GAME_COVER_FILES.put("崩坏：星穹铁道", "hkr.jpg");
        GAME_COVER_FILES.put("和平精英", "peacekeeper.jpg");  // 腾讯，无 Steam
        GAME_COVER_FILES.put("CSGO", "csgo.jpg");
        GAME_COVER_FILES.put("Valorant", "valorant.jpg");
        GAME_COVER_FILES.put("Apex英雄", "apex.jpg");
        GAME_COVER_FILES.put("鸣潮", "wuwa.jpg");
        GAME_COVER_FILES.put("幻兽帕鲁", "palworld.jpg");
        GAME_COVER_FILES.put("地平线5", "fh5.jpg");
    }

    /**
     * 获取游戏封面图 URL
     * 优先级：数据库已有 URL > 本地静态图片 > Steam CDN
     */
    public String getCoverUrl(String gameName, String existingCoverUrl) {
        if (StringUtils.hasText(existingCoverUrl)) {
            return existingCoverUrl;
        }
        String localPath = getLocalCoverPath(gameName);
        if (localPath != null) {
            return localPath;
        }
        return null;
    }

    /**
     * 获取本地静态封面路径
     */
    private String getLocalCoverPath(String gameName) {
        // 精确匹配
        String fileName = GAME_COVER_FILES.get(gameName);
        if (fileName != null) {
            return LOCAL_COVER_BASE + "/" + fileName;
        }
        // 模糊匹配
        for (Map.Entry<String, String> entry : GAME_COVER_FILES.entrySet()) {
            String key = entry.getKey();
            if (gameName.contains(key) || key.contains(gameName)) {
                return LOCAL_COVER_BASE + "/" + entry.getValue();
            }
        }
        return null;
    }

    /**
     * 获取 Steam CDN 基础 URL（备用）
     */
    public String getSteamCdnBase() {
        return "https://steamcdn-a.akamaihd.net/steam/apps/%d/header.jpg";
    }

    /**
     * 获取所有已知游戏封面映射
     */
    public Map<String, String> getAllCoverMappings() {
        Map<String, String> result = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : GAME_COVER_FILES.entrySet()) {
            result.put(entry.getKey(), LOCAL_COVER_BASE + "/" + entry.getValue());
        }
        return result;
    }
}
