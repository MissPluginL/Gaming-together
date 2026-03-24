package com.gamerr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gamerr.entity.FriendRequest;
import java.util.List;

/**
 * 好友申请Service接口
 */
public interface FriendRequestService extends IService<FriendRequest> {

    /**
     * 发送好友申请
     * @param fromUserId 申请人ID
     * @param toUserId 被申请人ID
     * @param message 申请留言
     * @return 好友申请
     */
    FriendRequest sendRequest(Long fromUserId, Long toUserId, String message);

    /**
     * 获取收到的好友申请列表（待处理）
     * @param userId 用户ID
     * @return 申请列表
     */
    List<FriendRequest> getReceivedRequests(Long userId);

    /**
     * 获取发出的好友申请列表
     * @param userId 用户ID
     * @return 申请列表
     */
    List<FriendRequest> getSentRequests(Long userId);

    /**
     * 处理好友申请（同意/拒绝）
     * @param requestId 申请ID
     * @param userId 处理人ID（被申请人）
     * @param accept 是否同意
     * @return 是否成功
     */
    boolean handleRequest(Long requestId, Long userId, boolean accept);

    /**
     * 检查是否存在待处理的申请
     * @param fromUserId 申请人ID
     * @param toUserId 被申请人ID
     * @return 是否存在待处理申请
     */
    boolean hasPendingRequest(Long fromUserId, Long toUserId);

    /**
     * 取消发出的申请
     * @param requestId 申请ID
     * @param userId 申请人ID
     */
    void cancelRequest(Long requestId, Long userId);
}
