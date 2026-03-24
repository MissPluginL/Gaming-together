package com.gamerr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gamerr.entity.Reservation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 预约Mapper接口
 */
@Mapper
public interface ReservationMapper extends BaseMapper<Reservation> {
}
