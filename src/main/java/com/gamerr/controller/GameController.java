package com.gamerr.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gamerr.dto.Result;
import com.gamerr.entity.Game;
import com.gamerr.service.GameCoverService;
import com.gamerr.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 游戏控制器
 */
@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameCoverService gameCoverService;

    /**
     * 分页获取游戏列表
     */
    @GetMapping
    public Result<IPage<Game>> getGamePage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category) {
        IPage<Game> page = gameService.getGamePage(current, size, keyword, category);
        return Result.success(page);
    }

    /**
     * 获取所有游戏（不分页，用于下拉框等）
     */
    @GetMapping("/all")
    public Result<List<Game>> getAllGames() {
        List<Game> games = gameService.list();
        // 补充封面图
        if (games != null) {
            for (Game game : games) {
                game.setCoverImage(gameCoverService.getCoverUrl(game.getName(), game.getCoverImage()));
            }
        }
        return Result.success(games);
    }

    /**
     * 根据ID获取游戏详情
     */
    @GetMapping("/{id}")
    public Result<Game> getGameById(@PathVariable Long id) {
        Game game = gameService.getGameById(id);
        return Result.success(game);
    }

    /**
     * 新增游戏
     */
    @PostMapping
    public Result<Game> createGame(@RequestBody Game game) {
        Game createdGame = gameService.createGame(game);
        return Result.success("创建成功", createdGame);
    }

    /**
     * 更新游戏
     */
    @PutMapping("/{id}")
    public Result<Game> updateGame(@PathVariable Long id, @RequestBody Game game) {
        Game updatedGame = gameService.updateGame(id, game);
        return Result.success("更新成功", updatedGame);
    }

    /**
     * 删除游戏
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return Result.success("删除成功", null);
    }
}
