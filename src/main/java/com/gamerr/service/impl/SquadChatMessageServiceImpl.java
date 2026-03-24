package com.gamerr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gamerr.entity.SquadChatMessage;
import com.gamerr.entity.User;
import com.gamerr.exception.BusinessException;
import com.gamerr.mapper.SquadChatMessageMapper;
import com.gamerr.mapper.UserMapper;
import com.gamerr.service.SquadChatMessageService;
import com.gamerr.service.SquadRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SquadChatMessageServiceImpl extends ServiceImpl<SquadChatMessageMapper, SquadChatMessage>
        implements SquadChatMessageService {

    @Autowired
    private SquadRoomService squadRoomService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public SquadChatMessage sendMessage(Long roomId, Long userId, String content) {
        var room = squadRoomService.getRoomById(roomId);
        if (room == null) {
            throw new BusinessException("房间不存在");
        }
        if (room.getStatus() == 0) {
            throw new BusinessException("房间已关闭，无法发言");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new BusinessException("消息不能为空");
        }
        SquadChatMessage msg = new SquadChatMessage();
        msg.setRoomId(roomId);
        msg.setUserId(userId);
        msg.setContent(content.trim());
        this.save(msg);
        enrichMessage(msg);
        return msg;
    }

    @Override
    public List<SquadChatMessage> getRoomMessages(Long roomId, int limit) {
        QueryWrapper<SquadChatMessage> q = new QueryWrapper<>();
        q.eq("room_id", roomId).orderByDesc("create_time").last("LIMIT " + limit);
        List<SquadChatMessage> msgs = this.list(q);
        msgs.forEach(this::enrichMessage);
        return msgs;
    }

    private void enrichMessage(SquadChatMessage msg) {
        if (msg.getUserId() != null) {
            User u = userMapper.selectById(msg.getUserId());
            if (u != null) {
                msg.setUserNickname(u.getNickname() != null ? u.getNickname() : u.getUsername());
            }
        }
    }
}
