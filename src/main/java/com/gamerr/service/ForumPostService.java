package com.gamerr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gamerr.entity.ForumPost;
import java.util.List;

/**
 * 论坛帖子Service接口
 */
public interface ForumPostService extends IService<ForumPost> {

    /**
     * 获取帖子列表（分页）
     */
    List<ForumPost> getPostList(int page, int size, Long gameId);

    /**
     * 获取帖子总数
     */
    long getPostCount(Long gameId);

    /**
     * 获取帖子详情
     */
    ForumPost getPostById(Long id);

    /**
     * 创建帖子
     */
    ForumPost createPost(Long userId, String title, String content, Long gameId);

    /**
     * 删除帖子
     */
    void deletePost(Long postId, Long userId);

    /**
     * 获取用户发布的帖子
     */
    List<ForumPost> getUserPosts(Long userId);
}
