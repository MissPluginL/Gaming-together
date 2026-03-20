package example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import example.entity.Report;
import example.mapper.ReportMapper;
import example.service.ReportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    private ReportMapper reportMapper;

    @Override
    public void addReport(Long userId, Long targetUserId, Long orderId, Integer type, String content, MultipartFile img) {
        Report r = new Report();
        r.setReportUserId(userId);
        reportMapper.insert(r);
    }

    @Override
    public IPage<?> myReport(Long userId) { return null; }
}