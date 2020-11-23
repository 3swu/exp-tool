package com.pr.exptool.service;

import com.google.common.collect.Lists;
import com.pr.exptool.entity.ExpRequest;
import com.pr.exptool.entity.ExpResult;
import com.pr.exptool.entity.ImageDO;
import com.pr.exptool.entity.ImageResult;
import com.pr.exptool.enums.ExpEnum;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author wulei
 * @date 2020/10/19
 */
@Slf4j
@Component
public class CommonService {

    @Autowired
    ImageService imageService;

    public boolean heartbeat(ExpEnum exp, String url) {
        try {
            String response = Unirest.get(url).asString().getBody();
            return "1".equals(response);
        } catch (Exception e) {
            log.info("{} service heartbeat failed", exp.getName());
            return false;
        }
    }

    public ExpResult run(ExpEnum expEnum, ExpRequest expRequest, String runUrl) {
        try {
            long startTime = System.currentTimeMillis();
            String predictionSavePath = imageService.getPredictionImageSavePath(expRequest.getImagePath());
            File response = Unirest.post(runUrl)
                    .field("image", new File(expRequest.getImagePath()))
                    .asFile(predictionSavePath)
                    .getBody();
            if (isFileExisted(predictionSavePath)) {
                ImageDO predictionImageDO = imageService.storePath(predictionSavePath);
                return ExpResult.builder()
                        .cost(System.currentTimeMillis() - startTime)
                        .msg("success")
                        .success(true)
                        .results(Lists.newArrayList(ImageResult.builder().imageKey(predictionImageDO.getKey()).build()))
                        .build();
            }
            return ExpResult.builder()
                    .cost(System.currentTimeMillis() - startTime)
                    .msg("could not receive image from exp service")
                    .success(false)
                    .results(Lists.newArrayList())
                    .build();
        } catch (Exception e) {
            log.error("{} experiment predict service error, request: {}", expEnum.getName(), expRequest.toString(), e);
            return ExpResult.builder().success(false).results(Lists.newArrayList()).build();
        }
    }

    private boolean isFileExisted(String path) {
        File file = new File(path);
        return file.exists();
    }
}
