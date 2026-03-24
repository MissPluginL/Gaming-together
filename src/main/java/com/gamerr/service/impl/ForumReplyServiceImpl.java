package com.gamerr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gamerr.entity.ForumReply;
import com.gamerr.entity.User;
import com.gamerr.exception.BusinessException;
import com.gamerr.mapper.ForumReplyMapper;
import com.gamerr.mapper.UserMapper;
import com.gamerr.service.ForumReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 论坛回复Service实现类
 */
@Service
public class ForumReplyServiceImpl extends ServiceImpl<ForumReplyMapper, ForumReply> implements ForumReplyService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<ForumReply> getRepliesByPostId(Long postId) {
        QueryWrapper<ForumReply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId);
        queryWrapper.eq("status", 1);
        queryWrapper.orderByAsc("create_time");

        List<ForumReply> replies = this.list(queryWrapper);

        // 补充用户信息
        for (ForumReply reply : replies) {
            User user = userMapper.selectById(reply.getUserId());
            if (user != null) {
                reply.setUsername(user.getUsername());
                reply.setNickname(user.getNickname());
            }
        }

        return replies;
    }

    @Override
    public ForumReply addReply(Long postId, Long userId, String content) {
        ForumReply reply = new ForumReply();
        reply.setPostId(postId);
        reply.setUserId(userId);
        reply.setContent(content);
        reply.setStatus(1);
        this.save(reply);

        User user = userMapper.selectById(userId);
        if (user != null) {
            reply.setUsername(user.getUsername());
            reply.setNickname(user.getNickname());
        }

        return reply;
    }

    @Override
    public void deleteReply(Long replyId, Long userId) {
        ForumReply reply = this.getById(replyId);
        if (reply == null) {
            throw new BusinessException("回复不存在");
        }
        if (!reply.getUserId().equals(userId)) {
            throw new BusinessException("无权删除此回复");
        }
        reply.setStatus(0);
        this.updateById(reply);
    }

    @Override
    public int getReplyCount(Long postId) {
        QueryWrapper<ForumReply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId);
        queryWrapper.eq("status", 1);
        return (int) this.count(queryWrapper);
    }
}
