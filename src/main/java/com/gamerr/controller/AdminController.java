package com.gamerr.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gamerr.dto.Result;
import com.gamerr.entity.ServiceOrder;
import com.gamerr.entity.User;
import com.gamerr.mapper.EvaluationMapper;
import com.gamerr.mapper.ServiceOrderMapper;
import com.gamerr.mapper.UserMapper;
import com.gamerr.service.ServiceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理端：数据统计、用户管理、头像审核、订单监管
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ServiceOrderMapper serviceOrderMapper;
    @Autowired
    private EvaluationMapper evaluationMapper;
    @Autowired
    private ServiceOrderService serviceOrderService;

    // ==================== 仪表盘 ====================

    @GetMapping("/dashboard")
    public Result<Map<String, Object>> dashboard() {
        Map<String, Object> m = new HashMap<>();
        m.put("userCount", userMapper.selectCount(null));
        m.put("orderCount", serviceOrderMapper.selectCount(null));
        m.put("evaluationCount", evaluationMapper.selectCount(null));
        QueryWrapper<ServiceOrder> qw = new QueryWrapper<>();
        qw.eq("status", 1);
        m.put("activeOrderCount", serviceOrderMapper.selectCount(qw));

        // 待审核凭据数
        QueryWrapper<ServiceOrder> aq = new QueryWrapper<>();
        aq.eq("submitted", 1).eq("audit_status", 0);
        m.put("pendingAuditCount", serviceOrderMapper.selectCount(aq));

        // 待审核头像数
        QueryWrapper<User> uq = new QueryWrapper<>();
        uq.eq("avatar_audit_status", 0);
        m.put("pendingAvatarCount", userMapper.selectCount(uq));

        return Result.success(m);
    }

    // ==================== 用户管理 ====================

    /**
     * 分页获取用户列表，支持搜索（用户名/昵称/手机/邮箱）
     */
    @GetMapping("/users")
    public Result<Map<String, Object>> listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String keyword) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.select(User.class, info -> !"password".equals(info.getColumn()));
        if (StringUtils.hasText(keyword)) {
            String kw = "%" + keyword.trim() + "%";
            qw.and(w -> w.like("username", kw)
                    .or().like("nickname", kw)
                    .or().like("phone", kw)
                    .or().like("email", kw));
        }
        qw.orderByDesc("create_time");
        Page<User> p = new Page<>(page, pageSize);
        Page<User> result = userMapper.selectPage(p, qw);
        result.getRecords().forEach(u -> u.setPassword(null));
        Map<String, Object> ret = new HashMap<>();
        ret.put("records", result.getRecords());
        ret.put("total", result.getTotal());
        ret.put("page", result.getCurrent());
        ret.put("pageSize", result.getSize());
        return Result.success(ret);
    }

    /**
     * 获取单个用户详情
     */
    @GetMapping("/users/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        User u = userMapper.selectById(id);
        if (u == null) {
            return Result.error("用户不存在");
        }
        u.setPassword(null);
        return Result.success(u);
    }

    /**
     * 启用/禁用用户
     */
    @PutMapping("/users/{id}/status")
    public Result<Void> toggleStatus(@PathVariable Long id, @RequestParam int status) {
        User u = userMapper.selectById(id);
        if (u == null) {
            return Result.error("用户不存在");
        }
        if (status != 0 && status != 1) {
            return Result.error("状态非法");
        }
        u.setStatus(status);
        userMapper.updateById(u);
        return Result.success(status == 1 ? "已启用" : "已禁用", null);
    }

    // ==================== 头像审核（原有） ====================

    @GetMapping("/users/avatar-pending")
    public Result<List<User>> avatarPending() {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("avatar_audit_status", 0);
        List<User> list = userMapper.selectList(qw);
        list.forEach(u -> u.setPassword(null));
        return Result.success(list);
    }

    @PutMapping("/users/{id}/avatar-audit")
    public Result<User> auditAvatar(@PathVariable Long id, @RequestParam int status) {
        User u = userMapper.selectById(id);
        if (u == null) {
            return Result.error("用户不存在");
        }
        if (status != 1 && status != 2) {
            return Result.error("状态非法");
        }
        u.setAvatarAuditStatus(status);
        userMapper.updateById(u);
        u.setPassword(null);
        return Result.success("处理成功", u);
    }

    // ==================== 订单管理 ====================

    /**
     * 获取全部订单（支持状态筛选）
     */
    @GetMapping("/orders/all")
    public Result<List<ServiceOrder>> allOrders(@RequestParam(required = false) Integer status) {
        QueryWrapper<ServiceOrder> qw = new QueryWrapper<>();
        qw.orderByDesc("create_time");
        if (status != null) {
            qw.eq("status", status);
        }
        return Result.success(serviceOrderMapper.selectList(qw));
    }

    /**
     * 获取待审核的订单（打手已提交凭据）
     */
    @GetMapping("/orders/pending-audit")
    public Result<List<ServiceOrder>> pendingAuditOrders() {
        QueryWrapper<ServiceOrder> qw = new QueryWrapper<>();
        qw.eq("submitted", 1).eq("audit_status", 0).orderByDesc("update_time");
        List<ServiceOrder> list = serviceOrderMapper.selectList(qw);
        return Result.success(list);
    }

    /**
     * 管理员审核订单凭据
     * @param approved true=通过（完成订单） false=拒绝（打手可重新提交）
     */
    @PutMapping("/orders/{id}/audit")
    public Result<Void> auditOrder(@PathVariable Long id,
                                   @RequestParam boolean approved,
                                   @RequestParam(required = false) String reason) {
        ServiceOrder o = serviceOrderMapper.selectById(id);
        if (o == null) {
            return Result.error("订单不存在");
        }
        if (o.getSubmitted() == null || o.getSubmitted() != 1 || o.getAuditStatus() == null || o.getAuditStatus() != 0) {
            return Result.error("该订单无需审核");
        }
        if (approved) {
            o.setStatus(2);         // 完成订单
            o.setAuditStatus(1);    // 审核通过
        } else {
            o.setStatus(1);         // 退回进行中
            o.setSubmitted(0);      // 打手可重新提交
            o.setAuditStatus(2);    // 审核拒绝
            o.setAuditRejectReason(reason);
        }
        serviceOrderMapper.updateById(o);
        return Result.success(approved ? "审核通过，订单已完成" : "已拒绝，打手可重新提交凭据", null);
    }
}
