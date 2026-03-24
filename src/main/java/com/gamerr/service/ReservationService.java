package com.gamerr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gamerr.entity.Reservation;

import java.util.List;

/**
 * 预约Service接口
 */
public interface ReservationService extends IService<Reservation> {

    /**
     * 分页查询预约列表
     */
    Page<Reservation> getReservationPage(Integer current, Integer size, Long userId);

    /**
     * 获取用户的预约列表
     */
    List<Reservation> getUserReservations(Long userId);

    /**
     * 创建预约
     */
    Reservation createReservation(Long userId, Long gameId);

    /**
     * 取消预约
     */
    void cancelReservation(Long id, Long userId);

    /**
     * 删除预约
     */
    void deleteReservation(Long id);
}
