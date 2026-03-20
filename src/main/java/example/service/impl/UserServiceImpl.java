package example.service.impl;

import example.dto.UserLoginDTO;
import example.dto.UserRegisterDTO;
import example.mapper.UserMapper;
import example.service.UserService;
import example.vo.UserVO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserVO login(UserLoginDTO dto) {
        return null;
    }

    @Override
    public void register(UserRegisterDTO dto) {
    }

    @Override
    public UserVO getUserInfo(Long userId) {
        return null;
    }

    @Override
    public void updatePwd(Long userId, String oldPwd, String newPwd) {
    }

    @Override
    public void updateProfile(Long userId, UserRegisterDTO dto) {
    }
}