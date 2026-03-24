package com.gamerr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gamerr.entity.Friendship;
import com.gamerr.exception.BusinessException;
import com.gamerr.mapper.FriendshipMapper;
import com.gamerr.service.FriendshipService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 好友关系Service实现类
 */
@Service
public class FriendshipServiceImpl extends ServiceImpl<FriendshipMapper, Friendship> implements FriendshipService {

    @Override
    public Friendship addFriend(Long userId, Long friendId, Integer relationType) {
        if (userId.equals(friendId)) {
            throw new BusinessException("不能添加自己为好友");
        }

        // 检查是否已经存在好友关系
        QueryWrapper<Friendship> qw = new QueryWrapper<>();
        qw.eq("user_id", userId).eq("friend_id", friendId);
        if (this.count(qw) > 0) {
            throw new BusinessException("该用户已在好友列表中");
        }

        Friendship friendship = new Friendship();
        friendship.setUserId(userId);
        friendship.setFriendId(friendId);
        friendship.setRelationType(relationType);
        this.save(friendship);
        return friendship;
    }

    @Override
    public List<Friendship> getFriends(Long userId) {
        QueryWrapper<Friendship> qw = new QueryWrapper<>();
        qw.eq("user_id", userId).orderByDesc("create_time");
        return this.list(qw);
    }

    @Override
    public List<Friendship> getFriendsByType(Long userId, Integer relationType) {
        QueryWrapper<Friendship> qw = new QueryWrapper<>();
        qw.eq("user_id", userId).eq("relation_type", relationType).orderByDesc("create_time");
        return this.list(qw);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        QueryWrapper<Friendship> qw = new QueryWrapper<>();
        qw.eq("user_id", userId).eq("friend_id", friendId);
        this.remove(qw);
    }

    @Override
    public boolean isFriend(Long userId, Long friendId) {
        QueryWrapper<Friendship> qw = new QueryWrapper<>();
        qw.eq("user_id", userId).eq("friend_id", friendId);
        return this.count(qw) > 0;
    }
}
