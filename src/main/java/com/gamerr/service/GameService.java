package com.gamerr.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gamerr.entity.Game;

/**
 * 游戏Service接口
 */
public interface GameService extends IService<Game> {

    /**
     * 分页查询游戏列表
     */
    IPage<Game> getGamePage(Integer current, Integer size, String keyword, String category);

    /**
     * 根据ID获取游戏详情
     */
    Game getGameById(Long id);

    /**
     * 创建游戏
     */
    Game createGame(Game game);

    /**
     * 更新游戏
     */
    Game updateGame(Long id, Game game);

    /**
     * 删除游戏
     */
    void deleteGame(Long id);
}
