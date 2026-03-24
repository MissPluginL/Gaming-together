package com.gamerr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gamerr.entity.Game;
import com.gamerr.entity.SquadRoom;
import com.gamerr.entity.SquadRoomMember;
import com.gamerr.entity.User;
import com.gamerr.exception.BusinessException;
import com.gamerr.mapper.GameMapper;
import com.gamerr.mapper.SquadRoomMapper;
import com.gamerr.mapper.SquadRoomMemberMapper;
import com.gamerr.mapper.UserMapper;
import com.gamerr.service.SquadRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 开黑房间Service实现类
 */
@Service
public class SquadRoomServiceImpl extends ServiceImpl<SquadRoomMapper, SquadRoom> implements SquadRoomService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GameMapper gameMapper;

    @Autowired
    private SquadRoomMemberMapper squadRoomMemberMapper;

    @Override
    public List<SquadRoom> getRoomList(int page, int size, Long gameId) {
        QueryWrapper<SquadRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1); // 只查询开放中的房间
        if (gameId != null) {
            queryWrapper.eq("game_id", gameId);
        }
        queryWrapper.orderByDesc("create_time");
        queryWrapper.last("LIMIT " + (page - 1) * size + "," + size);

        List<SquadRoom> rooms = this.list(queryWrapper);
        return enrichRooms(rooms);
    }

    @Override
    public long getRoomCount(Long gameId) {
        QueryWrapper<SquadRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        if (gameId != null) {
            queryWrapper.eq("game_id", gameId);
        }
        return this.count(queryWrapper);
    }

    @Override
    public SquadRoom getRoomById(Long id) {
        SquadRoom room = this.getById(id);
        if (room == null) {
            throw new BusinessException("房间不存在");
        }
        enrichRoom(room);
        return room;
    }

    @Override
    public SquadRoom createRoom(Long userId, String title, String description, Long gameId,
                                String roomNumber, String roomPassword, Integer maxPlayers, Integer roomType) {
        SquadRoom room = new SquadRoom();
        room.setTitle(title);
        room.setDescription(description);
        room.setGameId(gameId);
        room.setRoomNumber(roomNumber);
        room.setRoomPassword(roomPassword);
        room.setMaxPlayers(maxPlayers != null ? maxPlayers : 5); // 默认5人
        room.setCurrentPlayers(1); // 创建时房主人数为1
        room.setOwnerId(userId);
        room.setStatus(1); // 开放中
        room.setRoomType(roomType != null ? roomType : 1); // 默认匹配房间
        this.save(room);

        // 自动添加房主为成员（无 deleted 字段，避免触发全局逻辑删除）
        SquadRoomMember member = new SquadRoomMember();
        member.setRoomId(room.getId());
        member.setUserId(userId);
        squadRoomMemberMapper.insert(member);

        enrichRoom(room);
        return room;
    }

    @Override
    public SquadRoom updateRoom(Long roomId, Long userId, String title, String description,
                                String roomNumber, String roomPassword, Integer maxPlayers, Integer roomType) {
        SquadRoom room = this.getById(roomId);
        if (room == null) {
            throw new BusinessException("房间不存在");
        }
        if (!room.getOwnerId().equals(userId)) {
            throw new BusinessException("只有房主才能修改房间信息");
        }

        if (title != null && !title.trim().isEmpty()) {
            room.setTitle(title.trim());
        }
        if (description != null) {
            room.setDescription(description);
        }
        if (roomNumber != null) {
            room.setRoomNumber(roomNumber);
        }
        if (roomPassword != null) {
            room.setRoomPassword(roomPassword);
        }
        if (maxPlayers != null) {
            room.setMaxPlayers(maxPlayers);
        }
        if (roomType != null) {
            room.setRoomType(roomType);
        }

        this.updateById(room);
        enrichRoom(room);
        return room;
    }

    @Override
    public void closeRoom(Long roomId, Long userId, String role) {
        SquadRoom room = this.getById(roomId);
        if (room == null) {
            throw new BusinessException("房间不存在");
        }
        if (!room.getOwnerId().equals(userId) && !"ADMIN".equals(role)) {
            throw new BusinessException("只有房主或管理员才能关闭房间");
        }
        room.setStatus(0); // 已关闭
        this.updateById(room);
    }

    @Override
    public void joinRoom(Long roomId, Long userId) {
        SquadRoom room = this.getById(roomId);
        if (room == null) {
            throw new BusinessException("房间不存在");
        }
        if (room.getStatus() == 0) {
            throw new BusinessException("房间已关闭");
        }
        if (room.getCurrentPlayers() >= room.getMaxPlayers()) {
            throw new BusinessException("房间已满");
        }
        if (room.getOwnerId().equals(userId)) {
            throw new BusinessException("您已是房主");
        }
        // 检查用户是否已在房间中（deleted=0 为有效成员；库表列名 deleted，见实体类注释）
        if (isUserInRoom(roomId, userId)) {
            throw new BusinessException("您已在该房间中");
        }

        // 先物理删除 (room_id,user_id) 下任意残留行（含历史全局逻辑删除产生的 deleted=1），再插入，避免唯一索引冲突
        squadRoomMemberMapper.delete(
                new QueryWrapper<SquadRoomMember>()
                        .eq("room_id", roomId)
                        .eq("user_id", userId));

        SquadRoomMember member = new SquadRoomMember();
        member.setRoomId(roomId);
        member.setUserId(userId);
        squadRoomMemberMapper.insert(member);

        room.setCurrentPlayers(room.getCurrentPlayers() + 1);
        this.updateById(room);
    }

    @Override
    public void leaveRoom(Long roomId, Long userId) {
        SquadRoom room = this.getById(roomId);
        if (room == null) {
            throw new BusinessException("房间不存在");
        }

        // 物理删除成员记录（不使用逻辑删除，否则唯一索引冲突无法再次加入）
        QueryWrapper<SquadRoomMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_id", roomId);
        queryWrapper.eq("user_id", userId);
        squadRoomMemberMapper.delete(queryWrapper);

        if (room.getOwnerId().equals(userId)) {
            // 房主离开，关闭房间
            room.setStatus(0);
        } else {
            // 普通成员离开，减少人数
            if (room.getCurrentPlayers() > 1) {
                room.setCurrentPlayers(room.getCurrentPlayers() - 1);
            }
        }
        this.updateById(room);
    }

    @Override
    public List<SquadRoom> getUserRooms(Long userId) {
        QueryWrapper<SquadRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_id", userId);
        queryWrapper.orderByDesc("create_time");

        List<SquadRoom> rooms = this.list(queryWrapper);
        return enrichRooms(rooms);
    }

    @Override
    public List<SquadRoom> getRoomsByGame(Long gameId) {
        QueryWrapper<SquadRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("game_id", gameId);
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("create_time");

        List<SquadRoom> rooms = this.list(queryWrapper);
        return enrichRooms(rooms);
    }

    @Override
    public boolean isUserInRoom(Long roomId, Long userId) {
        QueryWrapper<SquadRoomMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_id", roomId);
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("deleted", 0);
        return squadRoomMemberMapper.exists(queryWrapper);
    }

    @Override
    public List<SquadRoomMember> getRoomMembers(Long roomId) {
        QueryWrapper<SquadRoomMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_id", roomId);
        queryWrapper.eq("deleted", 0);
        queryWrapper.orderByDesc("join_time");

        List<SquadRoomMember> members = squadRoomMemberMapper.selectList(queryWrapper);
        // 填充用户昵称
        for (SquadRoomMember member : members) {
            User user = userMapper.selectById(member.getUserId());
            if (user != null) {
                member.setUserNickname(user.getNickname());
            }
        }
        return members;
    }

    private List<SquadRoom> enrichRooms(List<SquadRoom> rooms) {
        for (SquadRoom room : rooms) {
            enrichRoom(room);
        }
        return rooms;
    }

    private void enrichRoom(SquadRoom room) {
        // 设置房主用户名和昵称
        User user = userMapper.selectById(room.getOwnerId());
        if (user != null) {
            room.setOwnerName(user.getUsername());
            room.setOwnerNickname(user.getNickname());
        }

        // 设置游戏名称
        if (room.getGameId() != null) {
            Game game = gameMapper.selectById(room.getGameId());
            if (game != null) {
                room.setGameName(game.getName());
            }
        }
    }
}
