package com.gamerr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gamerr.entity.FriendRequest;
import com.gamerr.entity.Friendship;
import com.gamerr.exception.BusinessException;
import com.gamerr.mapper.FriendRequestMapper;
import com.gamerr.service.FriendRequestService;
import com.gamerr.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 好友申请Service实现类
 */
@Service
public class FriendRequestServiceImpl extends ServiceImpl<FriendRequestMapper, FriendRequest> implements FriendRequestService {

    @Autowired
    private FriendshipService friendshipService;

    @Override
    @Transactional
    public FriendRequest sendRequest(Long fromUserId, Long toUserId, String message) {
        if (fromUserId.equals(toUserId)) {
            throw new BusinessException("不能添加自己为好友");
        }

        // 检查是否已经是好友
        if (friendshipService.isFriend(fromUserId, toUserId)) {
            throw new BusinessException("该用户已是您的好友");
        }

        // 检查是否已经存在待处理的申请
        if (hasPendingRequest(fromUserId, toUserId)) {
            throw new BusinessException("已发送过申请，请等待对方处理");
        }

        // 检查是否收到过对方的申请（如果收到过，则直接同意并创建好友关系）
        if (hasPendingRequest(toUserId, fromUserId)) {
            throw new BusinessException("对方已向您发送过申请，请先处理对方的申请");
        }

        FriendRequest request = new FriendRequest();
        request.setFromUserId(fromUserId);
        request.setToUserId(toUserId);
        request.setMessage(message);
        request.setStatus(0); // 待处理
        this.save(request);
        return request;
    }

    @Override
    public List<FriendRequest> getReceivedRequests(Long userId) {
        QueryWrapper<FriendRequest> qw = new QueryWrapper<>();
        qw.eq("to_user_id", userId)
          .eq("status", 0) // 只查询待处理的
          .orderByDesc("create_time");
        return this.list(qw);
    }

    @Override
    public List<FriendRequest> getSentRequests(Long userId) {
        QueryWrapper<FriendRequest> qw = new QueryWrapper<>();
        qw.eq("from_user_id", userId)
          .orderByDesc("create_time");
        return this.list(qw);
    }

    @Override
    @Transactional
    public boolean handleRequest(Long requestId, Long userId, boolean accept) {
        FriendRequest request = this.getById(requestId);
        if (request == null) {
            throw new BusinessException("申请不存在");
        }

        // 验证是否是接收者
        if (!request.getToUserId().equals(userId)) {
            throw new BusinessException("无权处理此申请");
        }

        // 验证申请状态
        if (request.getStatus() != 0) {
            throw new BusinessException("该申请已被处理");
        }

        if (accept) {
            // 同意申请：创建双向好友关系
            friendshipService.addFriend(userId, request.getFromUserId(), 0);
            friendshipService.addFriend(request.getFromUserId(), userId, 0);
            request.setStatus(1); // 已同意
        } else {
            // 拒绝申请
            request.setStatus(2); // 已拒绝
        }
        return this.updateById(request);
    }

    @Override
    public boolean hasPendingRequest(Long fromUserId, Long toUserId) {
        QueryWrapper<FriendRequest> qw = new QueryWrapper<>();
        qw.eq("from_user_id", fromUserId)
          .eq("to_user_id", toUserId)
          .eq("status", 0);
        return this.count(qw) > 0;
    }

    @Override
    @Transactional
    public void cancelRequest(Long requestId, Long userId) {
        FriendRequest request = this.getById(requestId);
        if (request == null) {
            throw new BusinessException("申请不存在");
        }

        if (!request.getFromUserId().equals(userId)) {
            throw new BusinessException("只能取消自己发送的申请");
        }

        if (request.getStatus() != 0) {
            throw new BusinessException("该申请已被处理，无法取消");
        }

        this.removeById(requestId);
    }
}
