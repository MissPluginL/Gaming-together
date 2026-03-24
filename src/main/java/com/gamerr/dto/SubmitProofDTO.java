package com.gamerr.dto;

import lombok.Data;

/**
 * 打手提交完成凭据 DTO
 */
@Data
public class SubmitProofDTO {
    /** 完成凭据图片URL列表（多个用逗号分隔） */
    private String proofUrls;
}
