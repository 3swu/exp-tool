package com.pr.exptool.service;

import com.pr.exptool.entity.ExpRequest;
import com.pr.exptool.entity.ExpResult;

public interface ExpService {
    boolean heartbeat();
    ExpResult run(ExpRequest expRequest);
}
