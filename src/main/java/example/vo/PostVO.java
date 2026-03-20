package example.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostVO {
    private Long id;
    private Long userId;
    private String nickname;
    private String avatar;
    private String title;
    private String content;
    private String gameName;
    private String gameArea;
    private LocalDateTime createTime;
}