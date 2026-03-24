package com.gamerr.controller;

import com.gamerr.dto.Result;
import com.gamerr.entity.SquadChatMessage;
import com.gamerr.entity.SquadRoom;
import com.gamerr.service.SquadChatMessageService;
import com.gamerr.service.SquadRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 开黑房间聊天（仅房间成员可查看和发送消息）
 */
@RestController
@RequestMapping("/squad")
public class SquadChatController {

    @Autowired
    private SquadChatMessageService chatService;

    @Autowired
    private SquadRoomService squadRoomService;

    /** 发送消息（需为房间成员） */
    @PostMapping("/rooms/{roomId}/messages")
    public Result<SquadChatMessage> sendMessage(
            @PathVariable Long roomId,
            @RequestBody Map<String, String> body,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        if (!isRoomMember(roomId, userId)) {
            return Result.error("仅房间成员可发送消息，请先加入房间");
        }
        String content = body.get("content");
        SquadChatMessage msg = chatService.sendMessage(roomId, userId, content);
        return Result.success(msg);
    }

    /** 获取最近消息（需为房间成员） */
    @GetMapping("/rooms/{roomId}/messages")
    public Result<List<SquadChatMessage>> getMessages(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "50") int limit,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        if (!isRoomMember(roomId, userId)) {
            return Result.error("仅房间成员可查看聊天记录，请先加入房间");
        }
        List<SquadChatMessage> msgs = chatService.getRoomMessages(roomId, limit);
        return Result.success(msgs);
    }

    /** 检查是否为房间成员（房主或成员表中） */
    private boolean isRoomMember(Long roomId, Long userId) {
        SquadRoom room = squadRoomService.getById(roomId);
        if (room == null) return false;
        if (room.getOwnerId().equals(userId)) return true;
        return squadRoomService.isUserInRoom(roomId, userId);
    }
}
