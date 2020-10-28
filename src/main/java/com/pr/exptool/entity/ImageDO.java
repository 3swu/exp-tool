package com.pr.exptool.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author wulei
 * @date 2020/10/17
 */
@Document("exp_image_collection")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDO {
    @Field("key")
    private String key;
    @Field("path")
    private String path;
}
