package example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import example.dto.AnnounceDTO;

public interface AdminService {
    Object dashboard();
    IPage<?> userList(Integer page, Integer size);
    void setUserStatus(Long id, Integer status);
    IPage<?> playerApplyList(Integer page, Integer size);
    void auditPlayer(Long id, Integer status);
    IPage<?> postList(Integer page, Integer size);
    void deletePost(Long id);
    IPage<?> orderList(Integer page, Integer size);
    IPage<?> reportList(Integer page, Integer size);
    void handleReport(Long id, String result);
    void addAnnounce(AnnounceDTO dto);
    IPage<?> announceList(Integer page, Integer size);
    Object announceDetail(Long id);
}