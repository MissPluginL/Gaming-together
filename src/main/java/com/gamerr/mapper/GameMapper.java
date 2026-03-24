package com.gamerr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gamerr.entity.Game;
import org.apache.ibatis.annotations.Mapper;

/**
 * 游戏Mapper接口
 */
@Mapper
public interface GameMapper extends BaseMapper<Game> {
}
