package com.gamerr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gamerr.entity.Friendship;
import java.util.List;

/**
 * 好友关系Service接口
 */
public interface FriendshipService extends IService<Friendship> {

    /**
     * 添加好友
     * @param userId 用户ID
     * @param friendId 好友ID
     * @param relationType 关系类型：0-普通好友，1-单主，2-陪玩/代练
     * @return 好友关系
     */
    Friendship addFriend(Long userId, Long friendId, Integer relationType);

    /**
     * 获取用户的好友列表
     * @param userId 用户ID
     * @return 好友列表
     */
    List<Friendship> getFriends(Long userId);

    /**
     * 获取特定类型的好友列表
     * @param userId 用户ID
     * @param relationType 关系类型
     * @return 好友列表
     */
    List<Friendship> getFriendsByType(Long userId, Integer relationType);

    /**
     * 删除好友
     * @param userId 用户ID
     * @param friendId 好友ID
     */
    void removeFriend(Long userId, Long friendId);

    /**
     * 检查是否为好友
     * @param userId 用户ID
     * @param friendId 好友ID
     * @return 是否为好友
     */
    boolean isFriend(Long userId, Long friendId);
}
