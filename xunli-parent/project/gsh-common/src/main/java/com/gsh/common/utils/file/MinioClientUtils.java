package com.gsh.common.utils.file;

import com.gsh.common.config.Global;
import com.gsh.common.utils.DateUtils;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

public class MinioClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(MinioClientUtils.class);

    public static final String GSH_BUCKET_NAME = Global.getConfig("minio.gshBuketName");;

    public static  MinioClientUtils instance;

    public static MinioClientUtils getInstance() throws InvalidPortException, InvalidEndpointException {
        if (null == instance) {
            instance = new MinioClientUtils();
        }
        return instance;
    }

    public String getAvatarFileName(String parentPath, Long factoryCode, String loginName) throws Exception {
        String filePath = parentPath + "/" + factoryCode + "/" + loginName + ".png";
        return filePath;
    }

    public String uploadAvatarFile(String parentPath, MultipartFile file, Long factoryCode, String loginName) throws Exception {
        String filePath = getAvatarFileName(parentPath, factoryCode, loginName);
        this.putObject(filePath, file.getInputStream());
        return filePath;
    }

    public String getFactoryLogoFileName(String parentPath, Long factoryCode, String field) throws Exception {
        String filePath = parentPath + "/" + factoryCode + "/" + field + ".png";
        return filePath;
    }

    public String uploadFactoryLogo(Long factoryCode, String field, MultipartFile file) throws Exception {
        String filePath = getFactoryLogoFileName("factory", factoryCode, field);
        this.putObject(filePath, file.getInputStream());
        return filePath;
    }

    public String uploadFile(String parentPath, MultipartFile file) throws Exception {
        String filePath = parentPath + "/" + FileUploadUtils.extractFilename(file);
        this.putObject(filePath, file.getInputStream());
        return filePath;
    }

    public String uploadFile(String parentPath, String fileName, byte[] picBytes) throws Exception {
        String filePath =  parentPath + "/" + extractFilename(fileName);
        if(Global.isMinioFileStorageMode()){
            this.putObject(filePath, new ByteArrayInputStream(picBytes));
        }else{
            filePath = FileUploadUtils.getAbsoluteFile(Global.getProfile(), filePath).getPath();
            this.writeBytes(new ByteArrayInputStream(picBytes), new FileOutputStream(filePath));
        }
        return filePath;
    }

    /**
     * 编码文件名
     */
    public String extractFilename(String fileName)
    {
        String extension = FilenameUtils.getExtension(fileName);
        String filePath = DateUtils.datePath() + "/" + FileUploadUtils.encodingFilename(fileName) + "." + extension;
        return filePath;
    }

    /**
     * 输出指定文件的byte数组
     *
     * @param is 出入流
     * @param os 输出流
     * @return
     */
    public void writeBytes(InputStream is, OutputStream os) throws IOException
    {
        FileInputStream fis = null;
        try
        {
            byte[] b = new byte[8192];
            int length;
            while ((length = is.read(b)) > 0)
            {
                os.write(b, 0, length);
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            if (os != null)
            {
                try
                {
                    os.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 创建bucket
     *
     * @param bucketName bucket名称
     */
    public void createBucket(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidResponseException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, RegionConflictException {
        if (!MinioConfig.getInstance().getMinioClient().bucketExists(bucketName)) {
            MinioConfig.getInstance().getMinioClient().makeBucket(bucketName);
        }
    }

    /**
     * 获取全部bucket
     * <p>
     * https://docs.minio.io/cn/java-client-api-reference.html#listBuckets
     */
    public List<Bucket> getAllBuckets() throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidResponseException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return MinioConfig.getInstance().getMinioClient().listBuckets();
    }

    /**
     * 根据bucketName获取信息
     * @param bucketName bucket名称
     */
    public Optional<Bucket> getBucket(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidResponseException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return MinioConfig.getInstance().getMinioClient().listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }

    /**
     * 根据bucketName删除信息
     * @param bucketName bucket名称
     */
    public void removeBucket(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidResponseException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        MinioConfig.getInstance().getMinioClient().removeBucket(bucketName);
    }

    /**
     * 获取文件外链
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param expires    过期时间 <=7
     * @return url
     */
    public String getObjectURL(String bucketName, String objectName, Integer expires) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidExpiresRangeException, InvalidResponseException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return MinioConfig.getInstance().getMinioClient().presignedGetObject(bucketName, objectName, expires);
    }

    /**
     * 获取文件
     *
     * @param objectName 文件名称
     * @return 二进制流
     */
    public InputStream getObject(String objectName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InvalidResponseException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        logger.info(objectName);
        if(Global.isMinioFileStorageMode()){
            return MinioConfig.getInstance().getMinioClient().getObject(GSH_BUCKET_NAME, objectName);
        }else{
            return new FileInputStream(objectName);
        }
    }

    /**
     * 获取文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 二进制流
     */
    public InputStream getObject(String bucketName, String objectName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InvalidResponseException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return MinioConfig.getInstance().getMinioClient().getObject(bucketName, objectName);
    }

    /**
     * 上传文件
     *
     * @param objectName 文件名称
     * @param stream     文件流
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#putObject
     */
    public void putObject(String objectName, InputStream stream) throws Exception {
        logger.info(objectName);
        MinioConfig.getInstance().getMinioClient().putObject(GSH_BUCKET_NAME, objectName, stream,null,null,null,"application/octet-stream");
    }

    /**
     * 上传文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream     文件流
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#putObject
     */
    public void putObject(String bucketName, String objectName, InputStream stream) throws Exception {
        MinioConfig.getInstance().getMinioClient().putObject(bucketName, objectName, stream,null,null,null,"application/octet-stream");
    }

    /**
     * 上传文件
     *
     * @param bucketName  bucket名称
     * @param objectName  文件名称
     * @param stream      文件流
     * @param size        大小
     * @param contextType 类型
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#putObject
     */
    public void putObject(String bucketName, String objectName, InputStream stream, long size, String contextType) throws Exception {
        MinioConfig.getInstance().getMinioClient().putObject(bucketName, objectName, stream, size, null,null, contextType);
    }

    /**
     * 获取文件信息
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#statObject
     */
    public ObjectStat getObjectInfo(String bucketName, String objectName) throws Exception {
        return MinioConfig.getInstance().getMinioClient().statObject(bucketName, objectName);
    }

    /**
     * 删除文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#removeObject
     */
    public void removeObject(String bucketName, String objectName) throws Exception {
        MinioConfig.getInstance().getMinioClient().removeObject(bucketName, objectName);
    }

    public static void main(String[] args) throws Exception {
        MinioClientUtils.getInstance().putObject("gsh", "2019/11/01/"+String.valueOf(System.currentTimeMillis())+".jpg", new FileInputStream(new File("C:\\Users\\Administrator\\Pictures\\1.jpg")));
    }

    public static class MinioConfig {
        static MinioClient minioClient;
        static MinioConfig minioConfig;
        String accessKey = Global.getConfig("minio.accessKey");
        String secretKey = Global.getConfig("minio.secretKey");
        String endpoint = Global.getConfig("minio.endpoint");
        public MinioConfig() {};
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


}
