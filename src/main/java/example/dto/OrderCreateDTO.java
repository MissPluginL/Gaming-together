package example.dto;

import lombok.Data;

@Data
public class OrderCreateDTO {
    private Long masterId;
    private String gameName;
    private String serviceTime;
    private String remark;
}