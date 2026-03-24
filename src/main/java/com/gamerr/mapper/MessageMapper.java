package com.gamerr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gamerr.entity.Message;
import org.apache.ibatis.annotations.Mapper;

/**
 * 聊天消息Mapper
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
