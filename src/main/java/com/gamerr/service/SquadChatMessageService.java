package com.gamerr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gamerr.entity.SquadChatMessage;
import java.util.List;

public interface SquadChatMessageService extends IService<SquadChatMessage> {

    /** 发送消息 */
    SquadChatMessage sendMessage(Long roomId, Long userId, String content);

    /** 获取房间最近 N 条消息 */
    List<SquadChatMessage> getRoomMessages(Long roomId, int limit);
}
