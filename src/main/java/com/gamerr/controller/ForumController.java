package com.gamerr.controller;

import com.gamerr.dto.Result;
import com.gamerr.entity.ForumPost;
import com.gamerr.entity.ForumReply;
import com.gamerr.service.ForumPostService;
import com.gamerr.service.ForumReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 论坛控制器
 */
@RestController
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private ForumPostService forumPostService;

    @Autowired
    private ForumReplyService forumReplyService;

    /**
     * 获取帖子列表
     */
    @GetMapping("/posts")
    public Result<Map<String, Object>> getPostList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long gameId) {
        List<ForumPost> posts = forumPostService.getPostList(page, size, gameId);
        long total = forumPostService.getPostCount(gameId);
        return Result.success(Map.of(
            "list", posts,
            "total", total,
            "page", page,
            "size", size
        ));
    }

    /**
     * 获取帖子详情
     */
    @GetMapping("/posts/{id}")
    public Result<ForumPost> getPostDetail(@PathVariable Long id) {
        ForumPost post = forumPostService.getPostById(id);
        return Result.success(post);
    }

    /**
     * 创建帖子
     */
    @PostMapping("/posts")
    public Result<ForumPost> createPost(@RequestBody Map<String, Object> body,
                                        @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        String title = (String) body.get("title");
        String content = (String) body.get("content");
        Long gameId = body.get("gameId") != null ? ((Number) body.get("gameId")).longValue() : null;

        if (title == null || title.trim().isEmpty()) {
            return Result.error("标题不能为空");
        }
        if (content == null || content.trim().isEmpty()) {
            return Result.error("内容不能为空");
        }

        ForumPost post = forumPostService.createPost(userId, title.trim(), content.trim(), gameId);
        return Result.success("发布成功", post);
    }

    /**
     * 删除帖子
     */
    @DeleteMapping("/posts/{id}")
    public Result<Void> deletePost(@PathVariable Long id,
                                    @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        forumPostService.deletePost(id, userId);
        return Result.success("删除成功", null);
    }

    /**
     * 获取帖子回复
     */
    @GetMapping("/posts/{id}/replies")
    public Result<List<ForumReply>> getReplies(@PathVariable Long id) {
        List<ForumReply> replies = forumReplyService.getRepliesByPostId(id);
        return Result.success(replies);
    }

    /**
     * 添加回复
     */
    @PostMapping("/posts/{id}/replies")
    public Result<ForumReply> addReply(@PathVariable Long id,
                                        @RequestBody Map<String, String> body,
                                        @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        String content = body.get("content");
        if (content == null || content.trim().isEmpty()) {
            return Result.error("回复内容不能为空");
        }

        ForumReply reply = forumReplyService.addReply(id, userId, content.trim());
        return Result.success("回复成功", reply);
    }

    /**
     * 删除回复
     */
    @DeleteMapping("/replies/{id}")
    public Result<Void> deleteReply(@PathVariable Long id,
                                     @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        forumReplyService.deleteReply(id, userId);
        return Result.success("删除成功", null);
    }

    /**
     * 获取我的帖子
     */
    @GetMapping("/my/posts")
    public Result<List<ForumPost>> getMyPosts(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        List<ForumPost> posts = forumPostService.getUserPosts(userId);
        return Result.success(posts);
    }
}
