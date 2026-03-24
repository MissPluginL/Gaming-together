package com.gamerr.controller;

import com.gamerr.dto.Result;
import com.gamerr.entity.SquadRoom;
import com.gamerr.entity.SquadRoomMember;
import com.gamerr.service.SquadRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 开黑房间控制器
 */
@RestController
@RequestMapping("/squad")
public class SquadRoomController {

    @Autowired
    private SquadRoomService squadRoomService;

    /**
     * 获取房间列表
     */
    @GetMapping("/rooms")
    public Result<Map<String, Object>> getRoomList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long gameId) {
        List<SquadRoom> rooms = squadRoomService.getRoomList(page, size, gameId);
        long total = squadRoomService.getRoomCount(gameId);
        return Result.success(Map.of(
            "list", rooms,
            "total", total,
            "page", page,
            "size", size
        ));
    }

    /**
     * 获取房间详情
     */
    @GetMapping("/rooms/{id}")
    public Result<SquadRoom> getRoomDetail(@PathVariable Long id) {
        SquadRoom room = squadRoomService.getRoomById(id);
        return Result.success(room);
    }

    /**
     * 创建房间
     */
    @PostMapping("/rooms")
    public Result<SquadRoom> createRoom(@RequestBody Map<String, Object> body,
                                        @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }

        String title = (String) body.get("title");
        String description = (String) body.get("description");
        Long gameId = body.get("gameId") != null ? ((Number) body.get("gameId")).longValue() : null;
        String roomNumber = (String) body.get("roomNumber");
        String roomPassword = (String) body.get("roomPassword");
        Integer maxPlayers = body.get("maxPlayers") != null ? ((Number) body.get("maxPlayers")).intValue() : null;
        Integer roomType = body.get("roomType") != null ? ((Number) body.get("roomType")).intValue() : null;

        if (title == null || title.trim().isEmpty()) {
            return Result.error("房间标题不能为空");
        }
        if (description == null || description.trim().isEmpty()) {
            return Result.error("房间简介不能为空");
        }
        if (gameId == null) {
            return Result.error("请选择游戏");
        }

        SquadRoom room = squadRoomService.createRoom(
            userId, title.trim(), description.trim(), gameId,
            roomNumber, roomPassword, maxPlayers, roomType
        );
        return Result.success("创建成功", room);
    }

    /**
     * 更新房间
     */
    @PutMapping("/rooms/{id}")
    public Result<SquadRoom> updateRoom(@PathVariable Long id,
                                        @RequestBody Map<String, Object> body,
                                        @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }

        String title = (String) body.get("title");
        String description = (String) body.get("description");
        String roomNumber = (String) body.get("roomNumber");
        String roomPassword = (String) body.get("roomPassword");
        Integer maxPlayers = body.get("maxPlayers") != null ? ((Number) body.get("maxPlayers")).intValue() : null;
        Integer roomType = body.get("roomType") != null ? ((Number) body.get("roomType")).intValue() : null;

        SquadRoom room = squadRoomService.updateRoom(
            id, userId, title, description,
            roomNumber, roomPassword, maxPlayers, roomType
        );
        return Result.success("更新成功", room);
    }

    /**
     * 关闭房间（房主或管理员）
     */
    @DeleteMapping("/rooms/{id}")
    public Result<Void> closeRoom(@PathVariable Long id,
                                   @RequestHeader(value = "X-User-Id", required = false) Long userId,
                                   @RequestHeader(value = "X-User-Role", required = false) String role) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        squadRoomService.closeRoom(id, userId, role);
        return Result.success("关闭成功", null);
    }

    /**
     * 加入房间
     */
    @PostMapping("/rooms/{id}/join")
    public Result<Void> joinRoom(@PathVariable Long id,
                                 @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        squadRoomService.joinRoom(id, userId);
        return Result.success("加入成功", null);
    }

    /**
     * 离开房间
     */
    @PostMapping("/rooms/{id}/leave")
    public Result<Void> leaveRoom(@PathVariable Long id,
                                  @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        squadRoomService.leaveRoom(id, userId);
        return Result.success("离开成功", null);
    }

    /**
     * 获取我的房间
     */
    @GetMapping("/my/rooms")
    public Result<List<SquadRoom>> getMyRooms(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        List<SquadRoom> rooms = squadRoomService.getUserRooms(userId);
        return Result.success(rooms);
    }

    /**
     * 获取游戏下的房间
     */
    @GetMapping("/game/{gameId}/rooms")
    public Result<List<SquadRoom>> getGameRooms(@PathVariable Long gameId) {
        List<SquadRoom> rooms = squadRoomService.getRoomsByGame(gameId);
        return Result.success(rooms);
    }

    /**
     * 获取房间成员列表
     */
    @GetMapping("/rooms/{id}/members")
    public Result<List<SquadRoomMember>> getRoomMembers(@PathVariable Long id) {
        List<SquadRoomMember> members = squadRoomService.getRoomMembers(id);
        return Result.success(members);
    }

    /**
     * 检查用户是否在房间中
     */
    @GetMapping("/rooms/{id}/is-member")
    public Result<Boolean> checkIsMember(@PathVariable Long id,
                                        @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.success(false);
        }
        boolean isMember = squadRoomService.isUserInRoom(id, userId);
        return Result.success(isMember);
    }
}
