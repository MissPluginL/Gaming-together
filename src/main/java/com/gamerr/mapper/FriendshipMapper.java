package com.gamerr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gamerr.entity.Friendship;
import org.apache.ibatis.annotations.Mapper;

/**
 * 好友关系Mapper
 */
@Mapper
public interface FriendshipMapper extends BaseMapper<Friendship> {
}
