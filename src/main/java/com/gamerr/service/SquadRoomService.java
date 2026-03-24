package com.gamerr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gamerr.entity.SquadRoom;
import com.gamerr.entity.SquadRoomMember;
import java.util.List;

/**
 * 开黑房间Service接口
 */
public interface SquadRoomService extends IService<SquadRoom> {

    /**
     * 获取房间列表（分页）
     */
    List<SquadRoom> getRoomList(int page, int size, Long gameId);

    /**
     * 获取房间总数
     */
    long getRoomCount(Long gameId);

    /**
     * 获取房间详情
     */
    SquadRoom getRoomById(Long id);

    /**
     * 创建房间
     */
    SquadRoom createRoom(Long userId, String title, String description, Long gameId,
                         String roomNumber, String roomPassword, Integer maxPlayers, Integer roomType);

    /**
     * 更新房间
     */
    SquadRoom updateRoom(Long roomId, Long userId, String title, String description,
                        String roomNumber, String roomPassword, Integer maxPlayers, Integer roomType);

    /**
     * 关闭房间
     */
    void closeRoom(Long roomId, Long userId, String role);

    /**
     * 加入房间
     */
    void joinRoom(Long roomId, Long userId);

    /**
     * 离开房间
     */
    void leaveRoom(Long roomId, Long userId);

    /**
     * 获取用户创建的房间
     */
    List<SquadRoom> getUserRooms(Long userId);

    /**
     * 获取游戏下正在开放的房间
     */
    List<SquadRoom> getRoomsByGame(Long gameId);

    /**
     * 检查用户是否已加入房间
     */
    boolean isUserInRoom(Long roomId, Long userId);

    /**
     * 获取房间成员列表
     */
    List<SquadRoomMember> getRoomMembers(Long roomId);
}
