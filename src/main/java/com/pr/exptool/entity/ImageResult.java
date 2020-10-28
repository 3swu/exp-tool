package com.pr.exptool.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author wulei
 * @date 2020/10/16
 */
@Data
@Builder
public class ImageResult {
    Integer index;

    String msg;

    // 请求路径
    String imageKey;
}
