package com.pr.exptool.service;

import com.pr.exptool.dao.ImageDao;
import com.pr.exptool.entity.ImageDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @author wulei
 * @date 2020/10/17
 */
@Slf4j
@Component
public class ImageService {

    @Autowired
    ImageDao imageDao;

    @Autowired
    MongoTemplate mongoTemplate;

    private Path basePath;

    public ImageService(@Value("${image.path}") String imagePath) {
        try {
            basePath = Paths.get(imagePath);
            if (!Files.exists(basePath)) {
                Files.createDirectories(basePath);
                log.warn("base path not exists, create path {}", basePath.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("create base path error, path {}", basePath.toString());
        }
    }

    public ImageDO storePath(String path) {
        ImageDO storedImage = imageDao.findByPath(path);
        if (storedImage == null) {
            String uuidKey = UUID.randomUUID().toString().replaceAll("-", "");
            storedImage = ImageDO.builder().key(uuidKey).path(path).build();
            mongoTemplate.save(storedImage);
        }
        return storedImage;
    }

    public ImageDO receiveImage(MultipartFile multipartFile) {
        String filename = multipartFile.getOriginalFilename();
        Path fileStorePath = Paths.get(basePath.toString(), filename);
        try {
            multipartFile.transferTo(fileStorePath);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("store image file error, path: {}", fileStorePath.toString());
            return null;
        }
        return storePath(fileStorePath.toString());
    }

    public BufferedImage getImage(String key) {
        ImageDO imageDO = imageDao.findByKey(key);
        if (imageDO == null) {
            log.info("get no image for key: {}", key);
            return null;
        }
        try {
            return ImageIO.read(new FileInputStream(new File(imageDO.getPath())));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("read image error, path: {}", imageDO.getPath());
            return null;
        }
    }

    public String getPredictionImageSavePath(String path) {
        Path oriPath = Paths.get(path);
        return Paths.get(oriPath.getParent().toString(), "prediction-" + System.currentTimeMillis() + oriPath.getFileName().toString()).toString();
    }

    public String getPredictionZipSavePath(String path) {
        Path oriPath = Paths.get(path);
        String fileBaseName = oriPath.getFileName().toString().split("\\.")[0];
        return Paths.get(oriPath.getParent().toString(), "prediction-" + fileBaseName + ".zip").toString();
    }

    public String getBasePath() {
        return basePath.toString();
    }

}
