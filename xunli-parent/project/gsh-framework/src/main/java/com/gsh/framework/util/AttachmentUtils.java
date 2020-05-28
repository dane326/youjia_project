package com.gsh.framework.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gsh.common.config.Global;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 远程附件调用
 * 返回附件的URL地址
 *
 * Created by Administrator on 2019/9/30.
 */
public class AttachmentUtils {
    public static final Logger logger = LoggerFactory.getLogger(AttachmentUtils.class);
    private static Integer defaultDexpire = null;
    private static String cssBucketName = null;
    private static String jsBucketName = null;

    static {
        cssBucketName = Global.getConfig("attachment.cssBucketName");
        jsBucketName = Global.getConfig("attachment.jsBucketName");
        //这行代码debug启动报错 全局变量未配置
      //  defaultDexpire = Integer.parseInt(Global.getConfig("attachment.defaultDexpire"));
    }

    /**
     *
     * @param fileNameList css 文件名列表
     * @return
     *
     */
    public static HashMap<String,String> genCssUrls(List<String> fileNameList){
        HashMap<String,String> urlMap = new HashMap<>();
        if(null == fileNameList){
            return urlMap;
        }

        for (String fileName : fileNameList) {
            urlMap.put(fileName,genUrl(cssBucketName,fileName,defaultDexpire));
        }
        return urlMap;

    }
    /**
     *
     * @param fileNameList js 文件名列表
     * @return
     *
     */
    public static HashMap<String,String> genJsUrls(List<String> fileNameList){
        HashMap<String,String> urlMap = new HashMap<>();
        if(null == fileNameList){
            return urlMap;
        }

        for (String fileName : fileNameList) {
            urlMap.put(fileName,genUrl(jsBucketName,fileName,defaultDexpire));
        }
        return urlMap;
    }

    /**
     *
     * @param array JSONObject bucketName:目录 fileName：文件名 expires：有效时间(秒)
     * @return
     *
     */
    public static HashMap<String,String> genUrls(JSONArray array){
        HashMap<String,String> urlMap = new HashMap<>();
        if(null == array){
            return urlMap;
        }

        JSONObject obj = null;
        String bucketName = null;
        String fileName = null;
        int expires = 300;
        Iterator<?> it = array.iterator();
        while (it.hasNext()) {
            obj = (JSONObject) it.next();
            if(!obj.containsKey("bucketName") || !obj.containsKey("fileName")){
                continue;
            }
            bucketName = obj.getString("bucketName");
            fileName = obj.getString("fileName");
            try {
                expires = obj.getInteger("expires");
            }catch (Exception e){
                expires = defaultDexpire;
            }
            expires = obj.getInteger("expires");
            urlMap.put(fileName,genUrl(bucketName,fileName,expires));
        }
        return urlMap;

    }


    /**
     * 生成目录下的外链
     * @param bucketName    目录名
     * @param objectName    文件名  文件名前可以带目录名  eg：system/js/jquery.min.js
     * @param expires       url 有效期(秒) 到期后页面访问此地址失效
     */
    private static String genUrl(String bucketName,String objectName, Integer expires){
        MinioClient minioClient = MinioConfig.getInstance().getMinioClient();
        try {
            //http://182.151.199.4:11141/temp/%E8%8A%92%E7%A7%8D-%E9%9F%B3%E9%98%99%E8%AF%97%E5%90%AC%26%E8%B5%B5%E6%96%B9%E5%A9%A7.mp3?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=admin%2F20190929%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20190929T144318Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=78ede92e65784749ab635f6142ca4ec2867af06ed18f3e9112bba624fa74d808
            logger.info("获取附件参数：bucketName："+bucketName+" objectName:"+objectName+" expires"+expires);
            if(null == expires){
                expires = 300;//默认值
            }

            String url  = minioClient.presignedGetObject(bucketName, objectName,expires);
            logger.info("获取附件返回URL："+url);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
