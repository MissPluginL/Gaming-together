package com.gamerr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gamerr.dto.LoginVO;
import com.gamerr.dto.RegisterDTO;
import com.gamerr.dto.ResetPasswordDTO;
import com.gamerr.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 用户Service接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     */
    LoginVO register(RegisterDTO registerDTO);

    /**
     * 用户登录
     */
    LoginVO login(String username, String password);

    /**
     * 根据ID获取用户信息
     */
    User getUserById(Long id);

    /**
     * 根据访问者身份获取用户资料（他人隐藏时抛错；他人可见时脱敏）
     */
    User getUserProfileForViewer(Long targetId, Long viewerId);

    /**
     * 更新用户信息
     */
    User updateUser(Long id, User user);

    /**
     * 获取用户统计信息
     */
    Map<String, Object> getUserStatistics(Long userId);

    /**
     * 找回密码（演示验证码 123456）
     */
    void resetPassword(ResetPasswordDTO dto);

    /**
     * 搜索用户（按用户名、昵称、手机号、邮箱搜索）
     */
    List<User> searchUsers(String keyword);
}
