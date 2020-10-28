package com.pr.exptool.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author wulei
 * @date 2020/10/16
 */

@Data
@Builder
public class ExpResult {
    private boolean success;
    private long cost;
    private String msg;
    List<ImageResult> results;
}
