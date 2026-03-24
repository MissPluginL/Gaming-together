package com.gamerr.controller;

import com.gamerr.dto.Result;
import com.gamerr.entity.Message;
import com.gamerr.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 聊天消息控制器
 */
@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 发送消息
     */
    @PostMapping
    public Result<Message> sendMessage(@RequestBody Map<String, Object> body,
                                      @RequestHeader(value = "X-User-Id", required = false) Long senderId) {
        if (senderId == null) {
            return Result.error("请先登录");
        }
        Long receiverId = Long.valueOf(body.get("receiverId").toString());
        String content = body.get("content").toString();
        if (content == null || content.trim().isEmpty()) {
            return Result.error("消息内容不能为空");
        }
        Message message = messageService.sendMessage(senderId, receiverId, content.trim());
        return Result.success("发送成功", message);
    }

    /**
     * 获取与某个用户的消息记录
     */
    @GetMapping("/conversation/{otherUserId}")
    public Result<List<Message>> getMessages(@PathVariable Long otherUserId,
                                            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        List<Message> messages = messageService.getMessages(userId, otherUserId);
        return Result.success(messages);
    }

    /**
     * 获取未读消息数量
     */
    @GetMapping("/unread-count")
    public Result<Map<String, Integer>> getUnreadCount(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        int count = messageService.getTotalUnreadCount(userId);
        return Result.success(Map.of("count", count));
    }

    /**
     * 标记消息为已读
     */
    @PutMapping("/read/{otherUserId}")
    public Result<Void> markAsRead(@PathVariable Long otherUserId,
                                  @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        messageService.markAsRead(userId, otherUserId);
        return Result.success("已标记为已读", null);
    }
}
