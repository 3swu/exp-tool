package com.pr.exptool.controller;

import com.pr.exptool.entity.ExpRequest;
import com.pr.exptool.entity.ExpResult;
import com.pr.exptool.entity.ImageDO;
import com.pr.exptool.enums.ExpEnum;
import com.pr.exptool.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wulei
 * @date 2020/10/17
 */
@Slf4j
@RestController
@RequestMapping("/api/{expName}")
public class ExpController {

    @Autowired
    RetrievalService retrievalService;

    @Autowired
    DewarpService dewarpService;

    @Autowired
    DeblurService deblurService;

    @Autowired
    ImageService imageService;

    private ExpService getExpService(String expName) throws IllegalArgumentException{
        if (!StringUtils.isEmpty(expName)) {
            ExpEnum expEnum = ExpEnum.findByExpName(expName);
            if (expEnum == null) {
                throw new IllegalArgumentException();
            }
            switch (expEnum) {
                case RETRIEVAL:
                    return retrievalService;
                case DEWARP:
                    return dewarpService;
                case DEBLUR:
                    return deblurService;
                default:
                    throw new IllegalArgumentException();
            }
        }
        throw new IllegalArgumentException();
    }

    @PostMapping("/predict")
    public ExpResult predict(@PathVariable String expName, @RequestParam("image") MultipartFile multipartFile) {
        ImageDO imageDO = imageService.receiveImage(multipartFile);
        ExpRequest request = ExpRequest.builder().imagePath(imageDO.getPath()).build();
        return getExpService(expName).run(request);
    }

    @GetMapping("/heartbeat")
    public boolean heartbeat(@PathVariable String expName) {
        return getExpService(expName).heartbeat();
    }
}
