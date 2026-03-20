package example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("player_info")
public class PlayerInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String game;
    private String area;
    private String rank;
    private String rankImg;
    private String goodPosition;
    private Integer price;
    private Integer priceType;
    private String intro;
    private String qrCode;
    private String contact;
    private Integer status; // 0待审核 1通过 2驳回
    private Integer onlineStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
