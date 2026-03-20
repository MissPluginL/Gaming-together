package example.controller;

import example.common.Result;
import example.dto.OrderCreateDTO;
import example.service.OrderInfoService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/order")
public class OrderInfoController {

    @Resource
    private OrderInfoService orderInfoService;

    @PostMapping("/create")
    public Result<?> create(@RequestBody OrderCreateDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(orderInfoService.createOrder(userId, dto));
    }

    @GetMapping("/user-list")
    public Result<?> userList(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(orderInfoService.userOrderList(userId));
    }

    @GetMapping("/player-list")
    public Result<?> playerList(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(orderInfoService.playerOrderList(userId));
    }

    @PostMapping("/accept/{id}")
    public Result<?> accept(@PathVariable Long id, HttpServletRequest request) {
        Long playerId = (Long) request.getAttribute("userId");
        orderInfoService.acceptOrder(id, playerId);
        return Result.success();
    }

    @PostMapping("/reject/{id}")
    public Result<?> reject(@PathVariable Long id, HttpServletRequest request) {
        Long playerId = (Long) request.getAttribute("userId");
        orderInfoService.rejectOrder(id, playerId);
        return Result.success();
    }

    @PostMapping("/complete/{id}")
    public Result<?> complete(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        orderInfoService.completeOrder(id, userId);
        return Result.success();
    }

    @PostMapping("/cancel/{id}")
    public Result<?> cancel(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        orderInfoService.cancelOrder(id, userId);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    public Result<?> detail(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(orderInfoService.getOrderDetail(id, userId));
    }
}