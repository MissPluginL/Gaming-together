package com.gamerr.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gamerr.entity.Message;
import com.gamerr.mapper.MessageMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 聊天消息Service实现类
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Override
    public Message sendMessage(Long senderId, Long receiverId, String content) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        message.setType(0);
        message.setIsRead(0);
        message.setCreateTime(LocalDateTime.now());
        this.save(message);
        return message;
    }

    @Override
    public List<Message> getMessages(Long userId, Long otherUserId) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(w -> w
            .eq("sender_id", userId).eq("receiver_id", otherUserId)
            .or()
            .eq("sender_id", otherUserId).eq("receiver_id", userId)
        );
        queryWrapper.orderByAsc("create_time");
        List<Message> messages = this.list(queryWrapper);
        // 发送消息后自动标记为已读
        this.markAsRead(userId, otherUserId);
        return messages;
    }

    @Override
    public int getUnreadCount(Long userId) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver_id", userId);
        queryWrapper.eq("is_read", 0);
        return (int) this.count(queryWrapper);
    }

    @Override
    public void markAsRead(Long userId, Long otherUserId) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver_id", userId);
        queryWrapper.eq("sender_id", otherUserId);
        queryWrapper.eq("is_read", 0);
        Message update = new Message();
        update.setIsRead(1);
        this.update(update, queryWrapper);
    }

    @Override
    public int getTotalUnreadCount(Long userId) {
        return getUnreadCount(userId);
    }
}
