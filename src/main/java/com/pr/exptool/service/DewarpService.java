package com.pr.exptool.service;

import com.pr.exptool.entity.ExpRequest;
import com.pr.exptool.entity.ExpResult;
import com.pr.exptool.enums.ExpEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author wulei
 * @date 2020/10/17
 */
@Service
@Slf4j
public class DewarpService implements ExpService {

    @Value("${dewarp.heartbeat}")
    private String heartbeatUrl;

    @Value("${dewarp.run}")
    private String runUrl;

    @Autowired
    CommonService commonService;

    @Override
    public boolean heartbeat() {
        return commonService.heartbeat(ExpEnum.DEWARP, heartbeatUrl);
    }

    @Override
    public ExpResult run(ExpRequest expRequest) {
        return commonService.run(ExpEnum.DEWARP, expRequest, runUrl);
    }
}
