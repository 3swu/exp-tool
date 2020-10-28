package com.pr.exptool.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author wulei
 * @date 2020/10/16
 */
@Data
@Builder
@ToString
public class ExpRequest {
    String imagePath;
    Integer gpuId;
}
