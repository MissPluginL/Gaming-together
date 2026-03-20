package example.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderVO {
    private Long id;
    private Long userId;
    private Long masterId;
    private String gameName;
    private String serviceTime;
    private String remark;
    private Integer status;
    private LocalDateTime createTime;
}