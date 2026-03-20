package example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("report")
public class Report {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long reportUserId;
    private Long targetUserId;
    private Long orderId;
    private Integer type;
    private String content;
    private String img;
    private Integer status;
    private String result;
    private LocalDateTime createTime;
}