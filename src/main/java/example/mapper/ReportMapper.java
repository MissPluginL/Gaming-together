package example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import example.entity.Report;
@Mapper
public interface ReportMapper extends BaseMapper<Report> {
}