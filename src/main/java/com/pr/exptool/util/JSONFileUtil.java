package com.pr.exptool.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @author wulei
 * @date 2020/10/22
 */
@Slf4j
public class JSONFileUtil {

    private static final byte[] BUF = new byte[1024];

    public static String readFileAsString(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException();
        }
        InputStream inputStream = new FileInputStream(file);
        int len;
        StringBuilder sb = new StringBuilder();
        while ((len = inputStream.read(BUF)) > 0) {
            sb.append(new String(BUF, 0, len));
        }
        inputStream.close();
        return sb.toString();
    }
}
