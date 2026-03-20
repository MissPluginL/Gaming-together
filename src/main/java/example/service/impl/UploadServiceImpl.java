package example.service.impl;

import example.service.UploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadServiceImpl implements UploadService {
    @Override
    public String uploadImg(MultipartFile file) {
        return "https://default.png";
    }
}