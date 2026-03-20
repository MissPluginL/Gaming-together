package example.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GameMasterVO {
    private Long id;
    private Long userId;
    private String nickname;
    private String avatar;
    private String gameName;
    private String gameArea;
    private String level;
    private String introduction;
    private String price;
    private Integer status;
    private LocalDateTime createTime;
}