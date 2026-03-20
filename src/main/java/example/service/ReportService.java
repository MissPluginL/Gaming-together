package example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.multipart.MultipartFile;

public interface ReportService {
    void addReport(Long userId, Long targetUserId, Long orderId, Integer type, String content, MultipartFile img);
    IPage<?> myReport(Long userId);
}