package com.gamerr.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gamerr.dto.LoginVO;
import com.gamerr.dto.RegisterDTO;
import com.gamerr.dto.ResetPasswordDTO;
import com.gamerr.entity.User;
import com.gamerr.exception.BusinessException;
import com.gamerr.mapper.UserMapper;
import com.gamerr.util.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 用户Service实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public LoginVO register(RegisterDTO registerDTO) {
        // 验证两次密码是否一致
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new BusinessException("两次输入的密码不一致");
        }

        // 检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", registerDTO.getUsername());
        if (this.count(queryWrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }

        // 检查邮箱是否已存在（如果提供了邮箱）
        if (registerDTO.getEmail() != null && !registerDTO.getEmail().isEmpty()) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("email", registerDTO.getEmail());
            if (this.count(queryWrapper) > 0) {
                throw new BusinessException("邮箱已被注册");
            }
        }

        // 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword()); // 实际项目应加密存储
        user.setNickname(registerDTO.getNickname() != null ? registerDTO.getNickname() : registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        this.save(user);

        // 生成Token
        String token = JwtUtil.generateToken(user.getId(), user.getUsername());

        // 返回登录信息
        LoginVO loginVO = new LoginVO();
        BeanUtils.copyProperties(user, loginVO);
        loginVO.setToken(token);
        loginVO.setLoginTime(LocalDateTime.now());
        loginVO.setRole(resolveRole(user.getUsername()));

        return loginVO;
    }

    @Override
    public LoginVO login(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("password", password);
        queryWrapper.eq("status", 1);

        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        // 生成Token
        String token = JwtUtil.generateToken(user.getId(), user.getUsername());

        LoginVO loginVO = new LoginVO();
        BeanUtils.copyProperties(user, loginVO);
        loginVO.setToken(token);
        loginVO.setLoginTime(LocalDateTime.now());
        loginVO.setRole(resolveRole(user.getUsername()));

        return loginVO;
    }

    private static String resolveRole(String username) {
        return username != null && "admin".equalsIgnoreCase(username.trim()) ? "ADMIN" : "USER";
    }

    @Override
    public User getUserById(Long id) {
        User user = this.getById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        // 不返回密码
        user.setPassword(null);
        user.setRole(resolveRole(user.getUsername()));
        return user;
    }

    @Override
    public User getUserProfileForViewer(Long targetId, Long viewerId) {
        User user = this.getById(targetId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setPassword(null);
        user.setRole(resolveRole(user.getUsername()));

        boolean isSelf = viewerId != null && viewerId.equals(targetId);
        if (isSelf) {
            return user;
        }

        Integer hidden = user.getProfileHidden();
        if (hidden != null && hidden == 1) {
            throw new BusinessException("该用户信息保密");
        }

        // 他人查看：脱敏
        user.setEmail(null);
        user.setPhone(null);
        return user;
    }

    @Override
    public User updateUser(Long id, User user) {
        User existUser = this.getById(id);
        if (existUser == null) {
            throw new BusinessException("用户不存在");
        }

        existUser.setNickname(user.getNickname());
        existUser.setEmail(user.getEmail());
        existUser.setPhone(user.getPhone());
        existUser.setGameRank(user.getGameRank());
        existUser.setSkillTags(user.getSkillTags());
        if (user.getProfileHidden() != null) {
            existUser.setProfileHidden(user.getProfileHidden());
        }
        if (user.getAvatar() != null && !Objects.equals(user.getAvatar(), existUser.getAvatar())) {
            existUser.setAvatar(user.getAvatar());
            existUser.setAvatarAuditStatus(0);
        }
        existUser.setUpdateTime(LocalDateTime.now());

        this.updateById(existUser);
        existUser.setPassword(null);
        return existUser;
    }

    @Override
    public Map<String, Object> getUserStatistics(Long userId) {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("userId", userId);
        // 这里可以添加更多统计信息，如预约数量等
        return statistics;
    }

    @Override
    public void resetPassword(ResetPasswordDTO dto) {
        if (!"123456".equals(dto.getVerifyCode())) {
            throw new BusinessException("验证码错误（演示环境固定为 123456）");
        }
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("phone", dto.getPhone());
        User user = this.getOne(qw);
        if (user == null) {
            throw new BusinessException("该手机号未注册，请先在个人中心绑定手机号");
        }
        user.setPassword(dto.getNewPassword());
        user.setUpdateTime(LocalDateTime.now());
        this.updateById(user);
    }

    @Override
    public List<User> searchUsers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }
        String kw = "%" + keyword.trim() + "%";
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(w -> w
            .like("username", kw)
            .or()
            .like("nickname", kw)
            .or()
            .like("phone", kw)
            .or()
            .like("email", kw)
        );
        queryWrapper.eq("status", 1);
        queryWrapper.select("id", "username", "nickname", "phone", "email", "avatar", "game_rank", "skill_tags", "create_time");
        List<User> users = this.list(queryWrapper);
        for (User u : users) {
            u.setPassword(null);
            u.setRole(resolveRole(u.getUsername()));
        }
        return users;
    }
}
