package example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import example.dto.AnnounceDTO;
import example.entity.*;
import example.mapper.*;
import example.service.AdminService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private PlayerInfoMapper playerInfoMapper;
    @Resource
    private PostMapper postMapper;
    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private ReportMapper reportMapper;
    @Resource
    private AnnouncementMapper announcementMapper;

    @Override
    public Object dashboard() {
        return null;
    }

    @Override
    public IPage<?> userList(Integer page, Integer size) {
        return userMapper.selectPage(new Page<>(page, size), null);
    }

    @Override
    public void setUserStatus(Long id, Integer status) {
        User u = new User();
        u.setId(id);
        u.setStatus(status);
        userMapper.updateById(u);
    }

    @Override
    public IPage<?> playerApplyList(Integer page, Integer size) {
        return playerInfoMapper.selectPage(new Page<>(page, size), null);
    }

    @Override
    public void auditPlayer(Long id, Integer status) {
        PlayerInfo info = new PlayerInfo();
        info.setId(id);
        info.setStatus(status);
        playerInfoMapper.updateById(info);
    }

    @Override
    public IPage<?> postList(Integer page, Integer size) {
        return postMapper.selectPage(new Page<>(page, size), null);
    }

    @Override
    public void deletePost(Long id) {
        postMapper.deleteById(id);
    }

    @Override
    public IPage<?> orderList(Integer page, Integer size) {
        return orderInfoMapper.selectPage(new Page<>(page, size), null);
    }

    @Override
    public IPage<?> reportList(Integer page, Integer size) {
        return reportMapper.selectPage(new Page<>(page, size), null);
    }

    @Override
    public void handleReport(Long id, String result) {
        Report r = new Report();
        r.setId(id);
        r.setStatus(2);
        r.setResult(result);
        reportMapper.updateById(r);
    }

    @Override
    public void addAnnounce(AnnounceDTO dto) {
        Announcement a = new Announcement();
        a.setTitle(dto.getTitle());
        a.setContent(dto.getContent());
        announcementMapper.insert(a);
    }

    @Override
    public IPage<?> announceList(Integer page, Integer size) {
        return announcementMapper.selectPage(new Page<>(page, size), null);
    }

    @Override
    public Object announceDetail(Long id) {
        return announcementMapper.selectById(id);
    }
}