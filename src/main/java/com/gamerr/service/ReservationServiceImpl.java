package com.gamerr.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gamerr.entity.Reservation;
import com.gamerr.exception.BusinessException;
import com.gamerr.mapper.ReservationMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 预约Service实现类
 */
@Service
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservation> implements ReservationService {

    @Override
    public Page<Reservation> getReservationPage(Integer current, Integer size, Long userId) {
        Page<Reservation> page = new Page<>(current, size);
        QueryWrapper<Reservation> queryWrapper = new QueryWrapper<>();

        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }

        queryWrapper.orderByDesc("create_time");
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Reservation> getUserReservations(Long userId) {
        QueryWrapper<Reservation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("create_time");
        return this.list(queryWrapper);
    }

    @Override
    public Reservation createReservation(Long userId, Long gameId) {
        // 检查是否已预约
        QueryWrapper<Reservation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("game_id", gameId);
        queryWrapper.eq("status", 1);
        if (this.count(queryWrapper) > 0) {
            throw new BusinessException("您已预约过该游戏");
        }

        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setGameId(gameId);
        reservation.setReservationTime(LocalDateTime.now());
        reservation.setStatus(1);
        reservation.setCreateTime(LocalDateTime.now());
        reservation.setUpdateTime(LocalDateTime.now());

        this.save(reservation);
        return reservation;
    }

    @Override
    public void cancelReservation(Long id, Long userId) {
        Reservation reservation = this.getById(id);
        if (reservation == null) {
            throw new BusinessException("预约记录不存在");
        }
        if (!reservation.getUserId().equals(userId)) {
            throw new BusinessException("无权取消他人的预约");
        }
        if (reservation.getStatus() == 0) {
            throw new BusinessException("预约已被取消");
        }

        reservation.setStatus(0);
        reservation.setUpdateTime(LocalDateTime.now());
        this.updateById(reservation);
    }

    @Override
    public void deleteReservation(Long id) {
        if (this.getById(id) == null) {
            throw new BusinessException("预约记录不存在");
        }
        this.removeById(id);
    }
}
