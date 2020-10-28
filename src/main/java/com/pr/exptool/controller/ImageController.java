package com.pr.exptool.controller;

import com.pr.exptool.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;

/**
 * @author wulei
 * @date 2020/10/18
 */
@Slf4j
@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Autowired
    ImageService imageService;

    @GetMapping(value = "/get", produces = MediaType.IMAGE_PNG_VALUE)
    public BufferedImage getImage(String imageKey) {
        return imageService.getImage(imageKey);
    }
}
