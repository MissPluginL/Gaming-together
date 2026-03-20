package example.dto;

import lombok.Data;

@Data
public class EvaluateCreateDTO {
    private Long orderId;
    private Integer score;
    private String content;
}