package example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import example.dto.GameMasterApplyDTO;
import example.entity.PlayerInfo;
import example.mapper.PlayerInfoMapper;
import example.service.PlayerInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PlayerInfoServiceImpl implements PlayerInfoService {

    @Resource
    private PlayerInfoMapper playerInfoMapper;

    @Override
    public IPage<?> getPlayerList(Integer page, Integer size, String game, String rank) {
        QueryWrapper<PlayerInfo> qw = new QueryWrapper<>();
        qw.eq("status", 1);
        return playerInfoMapper.selectPage(new Page<>(page, size), qw);
    }

    @Override
    public Object getDetail(Long userId) {
        QueryWrapper<PlayerInfo> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        return playerInfoMapper.selectOne(qw);
    }

    @Override
    public void apply(Long userId, GameMasterApplyDTO dto) {
        PlayerInfo info = new PlayerInfo();
        info.setUserId(userId);
        info.setStatus(0);
        playerInfoMapper.insert(info);
    }

    @Override
    public void updateInfo(Long userId, GameMasterApplyDTO dto) {}

    @Override
    public void setOnlineStatus(Long userId, Integer onlineStatus) {}
}