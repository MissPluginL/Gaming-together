package example.dto;

import lombok.Data;

@Data
public class ReportCreateDTO {
    private String targetType;
    private Long targetId;
    private String reason;
    private String content;
}