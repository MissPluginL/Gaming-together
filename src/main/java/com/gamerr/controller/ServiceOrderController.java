package com.gamerr.controller;

import com.gamerr.dto.EvaluationCreateDTO;
import com.gamerr.dto.Result;
import com.gamerr.dto.SubmitProofDTO;
import com.gamerr.entity.Evaluation;
import com.gamerr.entity.ServiceOrder;
import com.gamerr.service.ServiceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 订单：发单、接单、支付(模拟)、完成、评价
 */
@RestController
@RequestMapping("/orders")
public class ServiceOrderController {

    @Autowired
    private ServiceOrderService serviceOrderService;

    @PostMapping
    public Result<ServiceOrder> create(@RequestHeader(value = "X-User-Id", required = false) Long userId,
                                       @RequestBody ServiceOrder body) {
        return Result.success("发布成功", serviceOrderService.createOrder(userId, body));
    }

    /** 接单大厅（已支付待接单的订单） */
    @GetMapping("/lobby")
    public Result<List<ServiceOrder>> lobby() {
        return Result.success(serviceOrderService.listOpenOrders());
    }

    /** 按游戏获取接单大厅订单 */
    @GetMapping("/lobby/game")
    public Result<List<ServiceOrder>> lobbyByGame(@RequestParam(required = false) String gameName) {
        return Result.success(serviceOrderService.listOpenOrdersByGame(gameName));
    }

    @GetMapping("/my/published")
    public Result<List<ServiceOrder>> myPublished(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        return Result.success(serviceOrderService.listMyPublished(userId));
    }

    @GetMapping("/my/taking")
    public Result<List<ServiceOrder>> myTaking(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        return Result.success(serviceOrderService.listMyTaking(userId));
    }

    @GetMapping("/detail/{id}")
    public Result<ServiceOrder> detail(@PathVariable Long id) {
        return Result.success(serviceOrderService.getDetail(id));
    }

    @PostMapping("/{id}/pay")
    public Result<Void> pay(@PathVariable Long id,
                            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        serviceOrderService.payOrder(id, userId);
        return Result.success("支付成功（模拟）", null);
    }

    @PutMapping("/{id}/accept")
    public Result<Void> accept(@PathVariable Long id,
                               @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        serviceOrderService.acceptOrder(id, userId);
        return Result.success("接单成功", null);
    }

    /** 打手上交完成凭据（图片URL，多个逗号分隔） */
    @PutMapping("/{id}/submit-proof")
    public Result<Void> submitProof(@PathVariable Long id,
                                    @RequestHeader(value = "X-User-Id", required = false) Long userId,
                                    @RequestBody SubmitProofDTO dto) {
        serviceOrderService.submitProof(id, userId, dto.getProofUrls());
        return Result.success("凭据已提交，等待管理员审核", null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> cancel(@PathVariable Long id,
                               @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        serviceOrderService.cancelOrder(id, userId);
        return Result.success("已取消", null);
    }

    @PostMapping("/{id}/evaluation")
    public Result<Evaluation> evaluate(@PathVariable Long id,
                                       @RequestHeader(value = "X-User-Id", required = false) Long userId,
                                       @Validated @RequestBody EvaluationCreateDTO dto) {
        return Result.success("评价成功", serviceOrderService.evaluateOrder(id, userId, dto));
    }
}
