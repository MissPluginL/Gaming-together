package example.dto;

import lombok.Data;

@Data
public class GameMasterApplyDTO {
    private String gameName;
    private String gameArea;
    private String level;
    private String introduction;
    private String price;
}