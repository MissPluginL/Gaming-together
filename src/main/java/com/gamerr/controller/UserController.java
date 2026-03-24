package com.gamerr.controller;

import com.gamerr.dto.LoginDTO;
import com.gamerr.dto.LoginVO;
import com.gamerr.dto.RegisterDTO;
import com.gamerr.dto.ResetPasswordDTO;
import com.gamerr.dto.Result;
import com.gamerr.entity.User;
import com.gamerr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<LoginVO> register(@Validated @RequestBody RegisterDTO registerDTO) {
        LoginVO loginVO = userService.register(registerDTO);
        return Result.success("注册成功", loginVO);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
        return Result.success("登录成功", loginVO);
    }

    /**
     * 找回密码（演示：短信验证码固定 123456）
     */
    @PostMapping("/reset-password")
    public Result<Void> resetPassword(@Valid @RequestBody ResetPasswordDTO dto) {
        userService.resetPassword(dto);
        return Result.success("密码已重置，请使用新密码登录", null);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<User> getCurrentUser(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        User user = userService.getUserById(userId);
        return Result.success(user);
    }

    /**
     * 根据ID获取用户信息（他人查看时尊重「不让他人查看个人中心」；需登录且带 X-User-Id）
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id,
                                    @RequestHeader(value = "X-User-Id", required = false) Long viewerId) {
        User user = userService.getUserProfileForViewer(id, viewerId);
        return Result.success(user);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody User user,
                                   @RequestHeader(value = "X-User-Id", required = false) Long currentUserId) {
        if (currentUserId == null || !currentUserId.equals(id)) {
            return Result.error("只能修改自己的资料");
        }
        User updatedUser = userService.updateUser(id, user);
        return Result.success("更新成功", updatedUser);
    }

    /**
     * 获取用户统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        Map<String, Object> statistics = userService.getUserStatistics(userId);
        return Result.success(statistics);
    }

    /**
     * 搜索用户（按用户名、昵称、手机号、邮箱）
     */
    @GetMapping("/search")
    public Result<List<User>> searchUsers(@RequestParam String keyword,
                                          @RequestHeader(value = "X-User-Id", required = false) Long currentUserId) {
        if (currentUserId == null) {
            return Result.error("请先登录");
        }
        List<User> users = userService.searchUsers(keyword);
        // 排除自己
        users.removeIf(u -> u.getId().equals(currentUserId));
        return Result.success(users);
    }
}
