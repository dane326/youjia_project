package com.gsh.framework.util;

import com.gsh.common.config.Global;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

public class MinioConfig {
    MinioClient minioClient;
    static MinioConfig minioConfig;

    private static String accessKey = null;
    private static String secretKey = null;
    private static String endpoint = null;

    private MinioConfig() {};

    static {
        accessKey = Global.getConfig("attachment.accessKey");
        secretKey = Global.getConfig("attachment.secretKey");
        endpoint = Global.getConfig("attachment.endpoint");
    }

    public static  MinioConfig getInstance() {
        if(null == minioConfig) {
            minioConfig = new MinioConfig();
        }
        return minioConfig;
    }
    public MinioClient getMinioClient() {
        try {
            minioClient = new MinioClient(endpoint, accessKey,secretKey);
        } catch (InvalidEndpointException e) {
            e.printStackTrace();
        } catch (InvalidPortException e) {
            e.printStackTrace();
        }
        return minioClient;
    }
}