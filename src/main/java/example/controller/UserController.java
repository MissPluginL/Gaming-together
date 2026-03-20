package example.controller;

import example.common.Result;
import example.dto.UserLoginDTO;
import example.dto.UserRegisterDTO;
import example.service.UserService;
import example.vo.UserVO;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Result<UserVO> login(@RequestBody UserLoginDTO dto) {
        UserVO vo = userService.login(dto);
        return Result.success(vo);
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody UserRegisterDTO dto) {
        userService.register(dto);
        return Result.success();
    }

    @GetMapping("/info")
    public Result<UserVO> info(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        UserVO vo = userService.getUserInfo(userId);
        return Result.success(vo);
    }

    @PostMapping("/update-pwd")
    public Result<?> updatePwd(@RequestParam String oldPwd, @RequestParam String newPwd, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        userService.updatePwd(userId, oldPwd, newPwd);
        return Result.success();
    }

    @PostMapping("/update-profile")
    public Result<?> updateProfile(@RequestBody UserRegisterDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        userService.updateProfile(userId, dto);
        return Result.success();
    }
}