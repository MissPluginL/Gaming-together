package example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import example.dto.GameMasterApplyDTO;

public interface PlayerInfoService {
    IPage<?> getPlayerList(Integer page, Integer size, String game, String rank);
    Object getDetail(Long userId);
    void apply(Long userId, GameMasterApplyDTO dto);
    void updateInfo(Long userId, GameMasterApplyDTO dto);
    void setOnlineStatus(Long userId, Integer onlineStatus);
}