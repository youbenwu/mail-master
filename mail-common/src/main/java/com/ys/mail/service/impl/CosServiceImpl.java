package com.ys.mail.service.impl;

import cn.hutool.core.io.FileUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.event.ProgressEvent;
import com.qcloud.cos.event.ProgressEventType;
import com.qcloud.cos.event.ProgressListener;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferProgress;
import com.qcloud.cos.transfer.Upload;
import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Credentials;
import com.tencent.cloud.Response;
import com.tencent.cloud.Scope;
import com.ys.mail.config.CosConfig;
import com.ys.mail.enums.CosFolderEnum;
import com.ys.mail.exception.ApiException;
import com.ys.mail.service.CosService;
import com.ys.mail.util.BlankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-01-05 13:59
 */
@Service
public class CosServiceImpl implements CosService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CosServiceImpl.class);

    private TransferManager transferManager;
    private COSClient cosClient;

    @Autowired
    private CosConfig cosConfig;

    @PostConstruct
    public void init() {
        // TODO:申请临时凭证
//        Response stsCredential = this.getSTSCredential(null);
//        cosClient = this.getCOSClient(stsCredential.credentials);
        cosClient = this.getOssClient(null);
        // 指定要上传到 COS 上的路径
        ExecutorService threadPool = Executors.newFixedThreadPool(cosConfig.getThreadPool());
        // 传入一个 threadpool, 若不传入线程池, 默认 TransferManager 中会生成一个单线程的线程池。
        transferManager = new TransferManager(cosClient, threadPool);
    }

    /**
     * 通用：根据永久密钥来获取COS临时凭证，防止客户端权限过大
     *
     * @param config Map配置，允许为空，默认使用配置yml中的配置
     * @return Response
     */
    @Override
    public Response getStsCredential(TreeMap<String, Object> config) {
        LOGGER.info("获取COS简单上传的临时凭证");
        try {
            if (BlankUtil.isEmpty(config)) config = new TreeMap<>();
            //这里的 SecretId 和 SecretKey 代表了用于申请临时密钥的永久身份（主账号、子账号等），子账号需要具有操作存储桶的权限。
            config.put("secretId", cosConfig.getSecretId());
            config.put("secretKey", cosConfig.getSecretKey());
            // 设置域名:
            // 临时密钥有效时长，单位是秒，默认 1800 秒，目前主账号最长 2 小时（即 7200 秒），子账号最长 36 小时（即 129600）秒
            config.put("durationSeconds", BlankUtil.isNotEmpty(config.get("durationSeconds")) ? config.get("durationSeconds") : cosConfig.getDurationSeconds());
            // 换成您的 bucket
            config.put("bucket", BlankUtil.isNotEmpty(config.get("bucket")) ? config.get("bucket") : cosConfig.getBucket());
            // 换成 bucket 所在地区
            config.put("region", BlankUtil.isNotEmpty(config.get("region")) ? config.get("region") : cosConfig.getRegion());

            // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的具体路径
            // 列举几种典型的前缀授权场景：
            // 1、允许访问所有对象："*"
            // 2、允许访问指定的对象："a/a1.txt", "b/b1.txt"
            // 3、允许访问指定前缀的对象："a*", "a/*", "b/*"
            // 如果填写了“*”，将允许用户访问所有资源；除非业务需要，否则请按照最小权限原则授予用户相应的访问权限范围。
            config.put("allowPrefixes", BlankUtil.isNotEmpty(config.get("allowPrefixes")) ? config.get("allowPrefixes") : cosConfig.getAllowPrefixes());
            // 密钥的权限列表。必须在这里指定本次临时密钥所需要的权限。
            // 简单上传、表单上传和分块上传需要以下的权限，其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
            config.put("allowActions", BlankUtil.isNotEmpty(config.get("allowActions")) ? config.get("allowActions") : cosConfig.getAllowActions());

            //设置 policy
            List<Scope> scopes = new ArrayList<Scope>();
            Scope scope = new Scope("name/cos:GetService", cosConfig.getBucket(), cosConfig.getRegion(), "*");
            scopes.add(scope);
            config.put("policy", CosStsClient.getPolicy(scopes));

            return CosStsClient.getCredential(config);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("获取COS临时凭证失败 !");
        }
    }

    /**
     * 初始化COS客户端
     *
     * @param credentials 临时凭证，为空时则返回永久密钥COS客户端
     * @return COS客户端
     */
    @Override
    public COSClient getOssClient(Credentials credentials) {
        LOGGER.info("初始化COS客户端");
        // 1 初始化用户身份信息（secretId, secretKey）。
        COSCredentials cred;
        if (BlankUtil.isNotEmpty(credentials))
            cred = new BasicSessionCredentials(credentials.tmpSecretId, credentials.tmpSecretKey, credentials.sessionToken);
        else cred = new BasicCOSCredentials(cosConfig.getSecretId(), cosConfig.getSecretKey());
        // 2 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(cosConfig.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        return new COSClient(cred, clientConfig);
    }

    /**
     * 同步上传
     *
     * @param bucketName 桶名称，默认使用配置文件
     * @param key        文件key
     * @param file       文件路径
     * @return URL
     */
    @Override
    public URL upload(String bucketName, CosFolderEnum cosFolder, String key, File file) {
        try {
            // 获取上传目录
            String folder = CosFolderEnum.IMAGES_FOLDER.value();
            if (BlankUtil.isNotEmpty(cosFolder)) folder = cosFolder.value();
            // 拼接完整的key
            key = folder + key;
            // 尝试检测网络
            this.isExistKey(key);
            // 获取桶
            if (BlankUtil.isEmpty(bucketName)) bucketName = cosConfig.getBucket();
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
            // 开始上传
            Upload upload = transferManager.upload(putObjectRequest);
            // 同步阻塞
            upload.waitForCompletion();
            // 记录结果
            LOGGER.info(upload.getDescription().replace("//", "/"));
            // 等待返回结果
            return cosClient.getObjectUrl(bucketName, key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ApiException("文件上传失败");
        } finally {
            if (file.exists()) FileUtil.del(file);
        }
    }

    @Override
    public URL upload(String key, File file) {
        return this.upload("", null, key, file);
    }

    @Override
    public URL upload(CosFolderEnum cosFolder, String key, File file) {
        return this.upload("", cosFolder, key, file);
    }

    @Override
    public URL asyncUpload(File file, CosFolderEnum cosFolder, String path) {
        try {
            // 获取上传目录
            String folder = CosFolderEnum.IMAGES_FOLDER.value();
            if (BlankUtil.isNotEmpty(cosFolder)) folder = cosFolder.value();
            // 拼接完整的path
            path = folder + path;
            // 尝试检测网络
            this.isExistKey(path);
            // 获取桶
            String bucketName = cosConfig.getBucket();
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, file);
            // 限流使用的单位是bit/s, 如最高1MB/s的上传带宽限制
            // putObjectRequest.setTrafficLimit(1 * 1024 * 1024 * 8)
            // 开始异步上传
            Upload upload = transferManager.upload(putObjectRequest);
            // 添加进度处理器
            // this.progressEvent(upload, file)
            // 记录结果
            LOGGER.info(upload.getDescription().replace("//", "/"));
            return cosClient.getObjectUrl(bucketName, path);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            if (file.exists()) FileUtil.del(file);
            throw new ApiException("文件上传失败");
        }
    }

    private void progressEvent(Upload upload, File file) {
        upload.addProgressListener(new ProgressListener() {
            @Override
            public void progressChanged(ProgressEvent progressEvent) {
                ProgressEventType eventType = progressEvent.getEventType();
                TransferProgress transfer = upload.getProgress();
                switch (eventType) {
                    case TRANSFER_PREPARING_EVENT:
                    case TRANSFER_STARTED_EVENT:
                    case TRANSFER_PART_STARTED_EVENT:
                        // 更新状态为等待中
                        System.out.println("等待中...");
                        break;
                    case REQUEST_BYTE_TRANSFER_EVENT:
                        // 更新进度、状态为处理中
                        System.out.println("处理中...");
                        long soFar = transfer.getBytesTransferred();
                        long total = transfer.getTotalBytesToTransfer();
                        double pct = transfer.getPercentTransferred();
                        System.out.printf("[%d / %d] = %.02f%%\n", soFar, total, pct);
                        break;
                    case TRANSFER_CANCELED_EVENT:
                        System.out.println("已取消...");
                        break;
                    case TRANSFER_PART_COMPLETED_EVENT:
                    case TRANSFER_COMPLETED_EVENT:
                        // 更新状态完成
                        System.out.println("成功...");
                        if (file.exists()) FileUtil.del(file);
                        break;
                    case TRANSFER_FAILED_EVENT:
                    case TRANSFER_PART_FAILED_EVENT:
                    case CLIENT_REQUEST_FAILED_EVENT:
                        // 更新状态为失败
                        System.out.println("失败...");
                        // 终止
                        upload.abort();
                        // 移除文件
                        if (file.exists()) {
                            System.out.println("删除文件");
                            FileUtil.del(file);
                        }
                        // 移除监听器
                        upload.removeProgressListener(this);
                        break;
                    default:
                }
            }
        });
    }

    @Override
    public String getOssPath() {
        // CDN: http://cos.huwing.cn/mail.huwing.cn/img/77e827c9ace741638850889d24207c62.jpg
        return String.format("http://%s/%s", cosConfig.getCdnDomain(), cosConfig.getUploadFolder());
    }

    @Override
    public Boolean isExistKey(String key) {
        try {
            key = cosConfig.getUploadFolder() + key;
            return cosClient.doesObjectExist(cosConfig.getBucket(), key);
        } catch (CosServiceException e) {
            e.printStackTrace();
            throw new ApiException("文件上传服务异常");
        } catch (CosClientException e) {
            e.printStackTrace();
            throw new ApiException("网络连接异常");
        }
    }

}
