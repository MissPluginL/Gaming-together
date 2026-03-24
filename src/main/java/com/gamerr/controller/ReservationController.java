package com.gamerr.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gamerr.dto.Result;
import com.gamerr.entity.Reservation;
import com.gamerr.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预约控制器
 */
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    /**
     * 分页获取预约列表
     */
    @GetMapping
    public Result<Page<Reservation>> getReservationPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId) {
        Page<Reservation> page = reservationService.getReservationPage(current, size, userId);
        return Result.success(page);
    }

    /**
     * 获取当前用户的预约列表
     */
    @GetMapping("/my")
    public Result<List<Reservation>> getMyReservations(
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        List<Reservation> reservations = reservationService.getUserReservations(userId);
        return Result.success(reservations);
    }

    /**
     * 创建预约
     */
    @PostMapping
    public Result<Reservation> createReservation(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestParam Long gameId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        Reservation reservation = reservationService.createReservation(userId, gameId);
        return Result.success("预约成功", reservation);
    }

    /**
     * 取消预约
     */
    @DeleteMapping("/{id}")
    public Result<Void> cancelReservation(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        reservationService.cancelReservation(id, userId);
        return Result.success("取消预约成功", null);
    }
}
