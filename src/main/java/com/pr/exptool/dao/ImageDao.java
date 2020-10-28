package com.pr.exptool.dao;

import com.pr.exptool.entity.ImageDO;
import org.springframework.data.repository.CrudRepository;

/**
 * @author wulei
 * @date 2020/10/17
 */

public interface ImageDao extends CrudRepository<ImageDO, String> {

    ImageDO findByKey(String key);

    ImageDO findByPath(String path);

}
