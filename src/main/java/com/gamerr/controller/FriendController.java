package com.gamerr.controller;

import com.gamerr.dto.Result;
import com.gamerr.entity.FriendRequest;
import com.gamerr.entity.Friendship;
import com.gamerr.entity.User;
import com.gamerr.mapper.UserMapper;
import com.gamerr.service.FriendRequestService;
import com.gamerr.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 好友关系控制器
 */
@RestController
@RequestMapping("/friends")
public class FriendController {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取所有好友列表
     */
    @GetMapping
    public Result<List<Map<String, Object>>> getFriends(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        List<Friendship> friendships = friendshipService.getFriends(userId);
        List<Map<String, Object>> result = enrichFriendships(friendships, userId);
        return Result.success(result);
    }

    /**
     * 获取特定类型的好友列表
     */
    @GetMapping("/type/{relationType}")
    public Result<List<Map<String, Object>>> getFriendsByType(
            @PathVariable Integer relationType,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        List<Friendship> friendships = friendshipService.getFriendsByType(userId, relationType);
        List<Map<String, Object>> result = enrichFriendships(friendships, userId);
        return Result.success(result);
    }

    /**
     * 添加好友
     */
    @PostMapping
    public Result<Friendship> addFriend(
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        Long friendId = ((Number) body.get("friendId")).longValue();
        Integer relationType = body.get("relationType") != null ?
                ((Number) body.get("relationType")).intValue() : 0;
        Friendship friendship = friendshipService.addFriend(userId, friendId, relationType);
        return Result.success("添加成功", friendship);
    }

    /**
     * 删除好友
     */
    @DeleteMapping("/{friendId}")
    public Result<Void> removeFriend(
            @PathVariable Long friendId,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        friendshipService.removeFriend(userId, friendId);
        return Result.success("删除成功", null);
    }

    // ==================== 好友申请相关接口 ====================

    /**
     * 发送好友申请
     */
    @PostMapping("/request")
    public Result<FriendRequest> sendFriendRequest(
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        Long toUserId = ((Number) body.get("toUserId")).longValue();
        String message = body.get("message") != null ? body.get("message").toString() : "";
        FriendRequest request = friendRequestService.sendRequest(userId, toUserId, message);
        return Result.success("申请已发送", request);
    }

    /**
     * 获取收到的好友申请列表（待处理）
     */
    @GetMapping("/requests/received")
    public Result<List<Map<String, Object>>> getReceivedRequests(
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        List<FriendRequest> requests = friendRequestService.getReceivedRequests(userId);
        List<Map<String, Object>> result = enrichRequests(requests, true);
        return Result.success(result);
    }

    /**
     * 获取发出的好友申请列表
     */
    @GetMapping("/requests/sent")
    public Result<List<Map<String, Object>>> getSentRequests(
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        List<FriendRequest> requests = friendRequestService.getSentRequests(userId);
        List<Map<String, Object>> result = enrichRequests(requests, false);
        return Result.success(result);
    }

    /**
     * 处理好友申请（同意/拒绝）
     */
    @PutMapping("/request/{requestId}")
    public Result<Void> handleFriendRequest(
            @PathVariable Long requestId,
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        Boolean accept = (Boolean) body.get("accept");
        friendRequestService.handleRequest(requestId, userId, accept != null && accept);
        return Result.success(accept != null && accept ? "已同意" : "已拒绝", null);
    }

    /**
     * 取消发出的好友申请
     */
    @DeleteMapping("/request/{requestId}")
    public Result<Void> cancelFriendRequest(
            @PathVariable Long requestId,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        friendRequestService.cancelRequest(requestId, userId);
        return Result.success("已取消申请", null);
    }

    /**
     * 补充好友申请信息
     */
    private List<Map<String, Object>> enrichRequests(List<FriendRequest> requests, boolean isReceived) {
        return requests.stream().map(r -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", r.getId());
            map.put("status", r.getStatus());
            map.put("message", r.getMessage());
            map.put("createTime", r.getCreateTime());

            // 获取对方用户信息
            Long otherUserId = isReceived ? r.getFromUserId() : r.getToUserId();
            User otherUser = userMapper.selectById(otherUserId);
            if (otherUser != null) {
                map.put("userId", otherUser.getId());
                map.put("username", otherUser.getUsername());
                map.put("nickname", otherUser.getNickname());
            }

            return map;
        }).collect(Collectors.toList());
    }

    /**
     * 补充好友信息
     */
    private List<Map<String, Object>> enrichFriendships(List<Friendship> friendships, Long currentUserId) {
        return friendships.stream().map(f -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", f.getId());
            map.put("relationType", f.getRelationType());
            map.put("remark", f.getRemark());
            map.put("createTime", f.getCreateTime());

            // 获取好友用户信息
            User friend = userMapper.selectById(f.getFriendId());
            if (friend != null) {
                map.put("friendId", friend.getId());
                map.put("friendUsername", friend.getUsername());
                map.put("friendNickname", friend.getNickname());
            }

            return map;
        }).collect(Collectors.toList());
    }
}
