package com.gamerr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gamerr.entity.ForumPost;
import com.gamerr.entity.Game;
import com.gamerr.entity.User;
import com.gamerr.exception.BusinessException;
import com.gamerr.mapper.ForumPostMapper;
import com.gamerr.mapper.GameMapper;
import com.gamerr.mapper.UserMapper;
import com.gamerr.service.ForumPostService;
import com.gamerr.service.ForumReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 论坛帖子Service实现类
 */
@Service
public class ForumPostServiceImpl extends ServiceImpl<ForumPostMapper, ForumPost> implements ForumPostService {

    @Autowired
    private ForumReplyService forumReplyService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GameMapper gameMapper;

    @Override
    public List<ForumPost> getPostList(int page, int size, Long gameId) {
        QueryWrapper<ForumPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        if (gameId != null) {
            queryWrapper.eq("game_id", gameId);
        }
        queryWrapper.orderByDesc("create_time");
        queryWrapper.last("LIMIT " + (page - 1) * size + "," + size);

        List<ForumPost> posts = this.list(queryWrapper);
        return enrichPosts(posts);
    }

    @Override
    public long getPostCount(Long gameId) {
        QueryWrapper<ForumPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        if (gameId != null) {
            queryWrapper.eq("game_id", gameId);
        }
        return this.count(queryWrapper);
    }

    @Override
    public ForumPost getPostById(Long id) {
        ForumPost post = this.getById(id);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }
        post.setReplyCount(forumReplyService.getReplyCount(id));
        enrichPost(post);
        return post;
    }

    @Override
    public ForumPost createPost(Long userId, String title, String content, Long gameId) {
        ForumPost post = new ForumPost();
        post.setUserId(userId);
        post.setTitle(title);
        post.setContent(content);
        post.setGameId(gameId);
        post.setStatus(1);
        this.save(post);

        enrichPost(post);
        return post;
    }

    @Override
    public void deletePost(Long postId, Long userId) {
        ForumPost post = this.getById(postId);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }
        if (!post.getUserId().equals(userId)) {
            throw new BusinessException("无权删除此帖子");
        }
        post.setStatus(0);
        this.updateById(post);
    }

    @Override
    public List<ForumPost> getUserPosts(Long userId) {
        QueryWrapper<ForumPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("create_time");

        List<ForumPost> posts = this.list(queryWrapper);
        return enrichPosts(posts);
    }

    private List<ForumPost> enrichPosts(List<ForumPost> posts) {
        for (ForumPost post : posts) {
            enrichPost(post);
        }
        return posts;
    }

    private void enrichPost(ForumPost post) {
        // 设置用户名和昵称
        User user = userMapper.selectById(post.getUserId());
        if (user != null) {
            post.setUsername(user.getUsername());
            post.setNickname(user.getNickname());
        }

        // 设置游戏名称
        if (post.getGameId() != null) {
            Game game = gameMapper.selectById(post.getGameId());
            if (game != null) {
                post.setGameName(game.getName());
            }
        }

        // 设置回复数量
        post.setReplyCount(forumReplyService.getReplyCount(post.getId()));
    }
}
