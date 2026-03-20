package example.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReportVO {
    private Long id;
    private Long userId;
    private String targetType;
    private Long targetId;
    private String reason;
    private String content;
    private Integer status;
    private LocalDateTime createTime;
}