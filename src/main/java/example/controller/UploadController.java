package example.controller;

import example.common.Result;
import example.service.UploadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Resource
    private UploadService uploadService;

    @PostMapping("/img")
    public Result<?> uploadImg(@RequestParam("file") MultipartFile file) {
        String url = uploadService.uploadImg(file);
        return Result.success(url);
    }
}