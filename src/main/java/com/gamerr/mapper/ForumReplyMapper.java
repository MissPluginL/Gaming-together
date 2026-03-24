package com.gamerr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gamerr.entity.ForumReply;
import org.apache.ibatis.annotations.Mapper;

/**
 * 论坛回复Mapper
 */
@Mapper
public interface ForumReplyMapper extends BaseMapper<ForumReply> {
}
