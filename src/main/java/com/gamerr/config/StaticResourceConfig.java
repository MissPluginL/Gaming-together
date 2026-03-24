package com.gamerr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 静态资源配置
 * 将 /api/images/** 映射到 classpath:/static/images/，解决图片跨域问题；
 * 将 /files/proof/** 映射到本地上传目录（与 UploadController 写入路径一致）。
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/images/**")
                .addResourceLocations("classpath:/static/images/");
        Path proofDir = Paths.get(uploadDir, "proof").toAbsolutePath();
        try {
            Files.createDirectories(proofDir);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        String location = "file:" + proofDir.toString().replace("\\", "/") + "/";
        registry.addResourceHandler("/files/proof/**")
                .addResourceLocations(location);
    }
}
