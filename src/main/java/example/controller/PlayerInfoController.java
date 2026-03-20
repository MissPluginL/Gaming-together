package example.controller;

import example.common.Result;
import example.dto.GameMasterApplyDTO;
import example.service.PlayerInfoService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/player")
public class PlayerInfoController {

    @Resource
    private PlayerInfoService playerInfoService;

    @GetMapping("/list")
    public Result<?> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String game,
            @RequestParam(required = false) String rank) {
        return Result.success(playerInfoService.getPlayerList(page, size, game, rank));
    }

    @GetMapping("/detail/{userId}")
    public Result<?> detail(@PathVariable Long userId) {
        return Result.success(playerInfoService.getDetail(userId));
    }

    @PostMapping("/apply")
    public Result<?> apply(@RequestBody GameMasterApplyDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        playerInfoService.apply(userId, dto);
        return Result.success();
    }

    @PostMapping("/update")
    public Result<?> update(@RequestBody GameMasterApplyDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        playerInfoService.updateInfo(userId, dto);
        return Result.success();
    }

    @PostMapping("/set-online")
    public Result<?> setOnline(@RequestParam Integer onlineStatus, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        playerInfoService.setOnlineStatus(userId, onlineStatus);
        return Result.success();
    }
}