package example.controller;

import example.common.Result;
import example.dto.AnnounceDTO;
import example.service.AdminService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @GetMapping("/dashboard")
    public Result<?> dashboard() {
        return Result.success(adminService.dashboard());
    }

    @GetMapping("/user-list")
    public Result<?> userList(@RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(adminService.userList(page, size));
    }

    @PostMapping("/set-user-status")
    public Result<?> setUserStatus(@RequestParam Long id, @RequestParam Integer status) {
        adminService.setUserStatus(id, status);
        return Result.success();
    }

    @GetMapping("/player-apply-list")
    public Result<?> playerApplyList(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(adminService.playerApplyList(page, size));
    }

    @PostMapping("/audit-player")
    public Result<?> auditPlayer(@RequestParam Long id, @RequestParam Integer status) {
        adminService.auditPlayer(id, status);
        return Result.success();
    }

    @GetMapping("/post-list")
    public Result<?> postList(@RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(adminService.postList(page, size));
    }

    @PostMapping("/delete-post/{id}")
    public Result<?> deletePost(@PathVariable Long id) {
        adminService.deletePost(id);
        return Result.success();
    }

    @GetMapping("/order-list")
    public Result<?> orderList(@RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(adminService.orderList(page, size));
    }

    @GetMapping("/report-list")
    public Result<?> reportList(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(adminService.reportList(page, size));
    }

    @PostMapping("/handle-report")
    public Result<?> handleReport(@RequestParam Long id, @RequestParam String result) {
        adminService.handleReport(id, result);
        return Result.success();
    }

    @PostMapping("/announce/add")
    public Result<?> addAnnounce(@RequestBody AnnounceDTO dto) {
        adminService.addAnnounce(dto);
        return Result.success();
    }

    @GetMapping("/announce/list")
    public Result<?> announceList(@RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(adminService.announceList(page, size));
    }

    @GetMapping("/announce/detail/{id}")
    public Result<?> announceDetail(@PathVariable Long id) {
        return Result.success(adminService.announceDetail(id));
    }
}