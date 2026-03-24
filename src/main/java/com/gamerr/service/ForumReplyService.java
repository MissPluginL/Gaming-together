package com.gamerr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gamerr.entity.ForumReply;
import java.util.List;

/**
 * 论坛回复Service接口
 */
public interface ForumReplyService extends IService<ForumReply> {

    /**
     * 获取帖子的回复列表
     */
    List<ForumReply> getRepliesByPostId(Long postId);

    /**
     * 添加回复
     */
    ForumReply addReply(Long postId, Long userId, String content);

    /**
     * 删除回复
     */
    void deleteReply(Long replyId, Long userId);

    /**
     * 获取回复数量
     */
    int getReplyCount(Long postId);
}
