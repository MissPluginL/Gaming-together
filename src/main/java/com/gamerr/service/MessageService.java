package com.gamerr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gamerr.entity.Message;

/**
 * 聊天消息Service接口
 */
public interface MessageService extends IService<Message> {

    /**
     * 发送消息
     */
    Message sendMessage(Long senderId, Long receiverId, String content);

    /**
     * 获取与某个用户的消息记录
     */
    java.util.List<Message> getMessages(Long userId, Long otherUserId);

    /**
     * 获取未读消息数量
     */
    int getUnreadCount(Long userId);

    /**
     * 标记消息为已读
     */
    void markAsRead(Long userId, Long otherUserId);

    /**
     * 获取所有未读消息数量
     */
    int getTotalUnreadCount(Long userId);
}
