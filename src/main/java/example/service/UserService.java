package example.service;

import example.dto.UserLoginDTO;
import example.dto.UserRegisterDTO;
import example.vo.UserVO;

public interface UserService {
    UserVO login(UserLoginDTO dto);
    void register(UserRegisterDTO dto);
    UserVO getUserInfo(Long userId);
    void updatePwd(Long userId, String oldPwd, String newPwd);
    void updateProfile(Long userId, UserRegisterDTO dto);
}