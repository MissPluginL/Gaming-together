package example.controller;

import example.common.Result;
import example.dto.EvaluateCreateDTO;
import example.service.EvaluateService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/evaluate")
public class EvaluateController {

    @Resource
    private EvaluateService evaluateService;

    @PostMapping("/add")
    public Result<?> add(@RequestBody EvaluateCreateDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        evaluateService.add(userId, dto);
        return Result.success();
    }

    @GetMapping("/my-evaluate")
    public Result<?> myEvaluate(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(evaluateService.myEvaluate(userId));
    }

    @GetMapping("/player-evaluate")
    public Result<?> playerEvaluate(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(evaluateService.playerEvaluate(userId));
    }

    @GetMapping("/list/{playerId}")
    public Result<?> list(@PathVariable Long playerId) {
        return Result.success(evaluateService.publicEvaluateList(playerId));
    }
}