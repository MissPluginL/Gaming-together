package example.dto;

import lombok.Data;

@Data
public class PostPublishDTO {
    private String title;
    private String content;
    private String gameName;
    private String gameArea;
}