package example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import example.entity.PlayerInfo;
@Mapper
public interface PlayerInfoMapper extends BaseMapper<PlayerInfo> {
}