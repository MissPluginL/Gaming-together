package com.gamerr.controller;

import com.gamerr.dto.Result;
import com.gamerr.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 本地文件上传（完成凭据等）
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @PostMapping("/proof")
    public Result<Map<String, String>> uploadProof(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) throws IOException {
        if (userId == null) {
            throw new BusinessException("请先登录");
        }
        if (file == null || file.isEmpty()) {
            throw new BusinessException("请选择图片文件");
        }
        String ct = file.getContentType();
        if (ct == null || !ct.startsWith("image/")) {
            throw new BusinessException("仅支持图片格式");
        }
        String ext = extFromContentType(ct);
        String name = UUID.randomUUID().toString().replace("-", "") + ext;
        Path dir = Paths.get(uploadDir, "proof").toAbsolutePath();
        Files.createDirectories(dir);
        Path target = dir.resolve(name);
        file.transferTo(target.toFile());

        String base = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + "/files/proof/" + name;
        Map<String, String> data = new HashMap<>(2);
        data.put("url", base);
        return Result.success(data);
    }

    private static String extFromContentType(String ct) {
        if (ct.contains("jpeg") || ct.contains("jpg")) {
            return ".jpg";
        }
        if (ct.contains("png")) {
            return ".png";
        }
        if (ct.contains("gif")) {
            return ".gif";
        }
        if (ct.contains("webp")) {
            return ".webp";
        }
        return ".bin";
    }
}
