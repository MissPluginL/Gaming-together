package example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("order_info")
public class OrderInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long userId;
    private Long playerId;
    private String game;
    private String area;
    private Integer price;
    private String remark;
    private Integer status; //1待接单 2已接单 3已完成 4已取消
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
