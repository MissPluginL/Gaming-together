package example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import example.entity.Post;
@Mapper
public interface PostMapper extends BaseMapper<Post> {
}