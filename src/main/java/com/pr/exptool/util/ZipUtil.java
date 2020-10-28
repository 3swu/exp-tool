package com.pr.exptool.util;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author wulei
 * @date 2020/10/22
 */

@Slf4j
public class ZipUtil {

    private static final byte[] BUF = new byte[1024];

    /**
     * decompress a zip file into base path for retrieval service
     * @param zipFilePath
     * @param dstPath
     * @return short path to abstract path Map
     */
    public static Map<String, String> decompress(String zipFilePath, String dstPath) throws IOException {
        Map<String, String> resultMap = Maps.newHashMap();
        ZipFile zipFile = new ZipFile(new File(zipFilePath));
        Enumeration entries = zipFile.entries();
        OutputStream outputStream = null;
        InputStream inputStream = null;
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            String entryName = zipEntry.getName();
            String entryDstPath = dstPath + File.separator + entryName;
            resultMap.put(entryName, entryDstPath);
            File entryDstFile = new File(entryDstPath);
            if (entryDstFile.exists()) {
                entryDstFile.createNewFile();
            }
            outputStream = new FileOutputStream(entryDstPath);
            inputStream = zipFile.getInputStream(zipEntry);
            int len;
            while ((len = inputStream.read(BUF)) > 0) {
                outputStream.write(BUF, 0, len);
            }
        }
        if (outputStream != null) {
            outputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        return resultMap;
    }
}
