package example.controller;

import example.common.Result;
import example.service.ReportService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Resource
    private ReportService reportService;

    @PostMapping("/add")
    public Result<?> add(
            @RequestParam Long targetUserId,
            @RequestParam(required = false) Long orderId,
            @RequestParam Integer type,
            @RequestParam String content,
            @RequestParam(required = false) MultipartFile img,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        reportService.addReport(userId, targetUserId, orderId, type, content, img);
        return Result.success();
    }

    @GetMapping("/my-report")
    public Result<?> myReport(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(reportService.myReport(userId));
    }
}