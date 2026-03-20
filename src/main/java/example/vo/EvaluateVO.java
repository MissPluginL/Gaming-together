package example.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EvaluateVO {
    private Long id;
    private Long orderId;
    private Long masterId;
    private String nickname;
    private String avatar;
    private Integer score;
    private String content;
    private LocalDateTime createTime;
}