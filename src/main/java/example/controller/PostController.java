package example.controller;

import example.common.Result;
import example.dto.PostPublishDTO;
import example.service.PostService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Resource
    private PostService postService;

    @GetMapping("/list")
    public Result<?> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String game) {
        return Result.success(postService.getPostList(page, size, game));
    }

    @GetMapping("/detail/{id}")
    public Result<?> detail(@PathVariable Long id) {
        return Result.success(postService.getDetail(id));
    }

    @PostMapping("/publish")
    public Result<?> publish(@RequestBody PostPublishDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        postService.publish(userId, dto);
        return Result.success();
    }

    @GetMapping("/my-post")
    public Result<?> myPost(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(postService.myPost(userId));
    }

    @PostMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        postService.delete(userId, id);
        return Result.success();
    }
}