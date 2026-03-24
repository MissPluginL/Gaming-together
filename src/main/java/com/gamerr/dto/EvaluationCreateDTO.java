package com.gamerr.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class EvaluationCreateDTO {

    @NotNull(message = "评分不能为空")
    @Min(1)
    @Max(5)
    private Integer rating;

    private String comment;
}
