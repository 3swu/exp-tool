package com.pr.exptool.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.pr.exptool.entity.ExpRequest;
import com.pr.exptool.entity.ExpResult;
import com.pr.exptool.entity.ImageResult;
import com.pr.exptool.enums.ExpEnum;
import com.pr.exptool.util.JSONFileUtil;
import com.pr.exptool.util.ZipUtil;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author wulei
 * @date 2020/10/17
 */
@Service
@Slf4j
public class RetrievalService implements ExpService{

    @Value("${retrieval.heartbeat}")
    private String heartbeatUrl;

    @Value("${retrieval.run}")
    private String runUrl;

    @Autowired
    ImageService imageService;

    @Autowired
    CommonService commonService;

    @Override
    public boolean heartbeat() {
        return commonService.heartbeat(ExpEnum.RETRIEVAL, heartbeatUrl);
    }

    @Override
    public ExpResult run(ExpRequest expRequest) {
        try {
            long startTime = System.currentTimeMillis();
            String predictionZipSavePath = imageService.getPredictionZipSavePath(expRequest.getImagePath());
            File response = Unirest.post(runUrl)
                    .field("image", new File(expRequest.getImagePath()))
                    .asFile(predictionZipSavePath)
                    .getBody();

            Map<String, String> resultPathMap = ZipUtil.decompress(predictionZipSavePath, imageService.getBasePath());
            String jsonInfo = JSONFileUtil.readFileAsString(resultPathMap.get("info.json"));
            JSONArray jsonArray = JSONArray.parseArray(jsonInfo);
            List<ImageResult> retrievalResults = Lists.newArrayList();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ImageResult imageResult = ImageResult.builder()
                        .index(jsonObject.getInteger("i"))
                        .msg(jsonObject.getString("msg"))
                        .imageKey(imageService.storePath(resultPathMap.get(jsonObject.getString("filename"))).getKey())
                        .build();
                retrievalResults.add(imageResult);
            }
            return ExpResult.builder()
                    .cost(System.currentTimeMillis() - startTime)
                    .msg("success")
                    .success(true)
                    .results(retrievalResults)
                    .build();
        } catch (IOException e) {
            log.error("retrieval experiment predict service error, request: {}", expRequest, e);
            return ExpResult.builder().success(false).results(Lists.newArrayList()).build();
        }
    }
}
