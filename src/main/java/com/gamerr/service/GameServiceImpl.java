package com.gamerr.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gamerr.entity.Game;
import com.gamerr.exception.BusinessException;
import com.gamerr.mapper.GameMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 游戏Service实现类
 */
@Service
public class GameServiceImpl extends ServiceImpl<GameMapper, Game> implements GameService {

    @Resource
    private GameCoverService gameCoverService;

    @Override
    public IPage<Game> getGamePage(Integer current, Integer size, String keyword, String category) {
        Page<Game> page = new Page<>(current, size);
        QueryWrapper<Game> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            queryWrapper.and(w -> w.like("name", keyword).or().like("description", keyword));
        }
        if (StringUtils.hasText(category)) {
            queryWrapper.eq("category", category);
        }

        queryWrapper.orderByDesc("create_time");
        IPage<Game> result = this.page(page, queryWrapper);
        // 自动补充封面图（优先数据库已有 > Steam CDN > IGDB）
        fillCoverImages(result.getRecords());
        return result;
    }

    @Override
    public Game getGameById(Long id) {
        Game game = this.getById(id);
        if (game == null) {
            throw new BusinessException("游戏不存在");
        }
        // 补充封面图
        game.setCoverImage(gameCoverService.getCoverUrl(game.getName(), game.getCoverImage()));
        return game;
    }

    @Override
    public Game createGame(Game game) {
        // 检查游戏名称是否已存在
        QueryWrapper<Game> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", game.getName());
        if (this.count(queryWrapper) > 0) {
            throw new BusinessException("游戏名称已存在");
        }

        game.setReservationCount(0);
        game.setCreateTime(LocalDateTime.now());
        game.setUpdateTime(LocalDateTime.now());
        // 自动补充封面图
        game.setCoverImage(gameCoverService.getCoverUrl(game.getName(), null));
        this.save(game);
        return game;
    }

    @Override
    public Game updateGame(Long id, Game game) {
        Game existGame = this.getById(id);
        if (existGame == null) {
            throw new BusinessException("游戏不存在");
        }

        // 检查名称冲突（排除自身）
        QueryWrapper<Game> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", game.getName());
        queryWrapper.ne("id", id);
        if (this.count(queryWrapper) > 0) {
            throw new BusinessException("游戏名称已存在");
        }

        existGame.setName(game.getName());
        existGame.setCategory(game.getCategory());
        existGame.setPlatform(game.getPlatform());
        existGame.setCoverImage(gameCoverService.getCoverUrl(game.getName(), game.getCoverImage()));
        existGame.setDescription(game.getDescription());
        existGame.setReleaseDate(game.getReleaseDate());
        existGame.setDeveloper(game.getDeveloper());
        existGame.setPrice(game.getPrice());
        existGame.setStatus(game.getStatus());
        existGame.setUpdateTime(LocalDateTime.now());

        this.updateById(existGame);
        return existGame;
    }

    @Override
    public void deleteGame(Long id) {
        if (this.getById(id) == null) {
            throw new BusinessException("游戏不存在");
        }
        this.removeById(id);
    }

    /**
     * 批量补充游戏封面图
     * 优先级：数据库已有 URL > Steam CDN > IGDB
     */
    private void fillCoverImages(List<Game> games) {
        if (games == null || games.isEmpty()) {
            return;
        }
        for (Game game : games) {
            String cover = gameCoverService.getCoverUrl(game.getName(), game.getCoverImage());
            game.setCoverImage(cover);
        }
    }
}
