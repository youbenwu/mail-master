package com.ys.mail.component;

import com.tencentcloudapi.cdn.v20180606.CdnClient;
import com.tencentcloudapi.cdn.v20180606.models.*;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.ys.mail.config.CosConfig;
import com.ys.mail.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

/**
 * CDN 服务
 *
 * @author CRH
 * @date 2022-05-23 16:57
 * @since 1.0
 */
@Slf4j
@Component
public class CdnService {

    @Autowired
    private CosConfig cosConfig;
    private CdnClient cdnClient;

    @PostConstruct
    public void init() {
        Credential cred = new Credential(cosConfig.getSecretId(), cosConfig.getSecretKey());
        cdnClient = new CdnClient(cred, "ap-guangzhou");
    }

    /**
     * 刷新URL
     *
     * @param urls 需要刷新的URL列表（完整的http资源路径）
     */
    public void purgeUrlsCache(List<String> urls) {
        log.info("【CDN】-purgeUrlsCache：\n{}", urls);
        PurgeUrlsCacheRequest request = new PurgeUrlsCacheRequest();
        request.setUrls(urls.toArray(new String[0]));
        try {
            cdnClient.PurgeUrlsCache(request);
        } catch (TencentCloudSDKException e) {
            throw new ApiException("刷新URL异常");
        }
    }

    /**
     * 刷新URL
     *
     * @param url 需要刷新的URL
     */
    public void purgeUrlsCache(String url) {
        this.purgeUrlsCache(Collections.singletonList(url));
    }

    /**
     * 刷新目录
     *
     * @param paths     需要刷新目录的列表(完整的目录)
     * @param flushType 刷新类型：flush->刷新产生更新的资源，delete->刷新全部资源
     */
    public void purgePathCache(List<String> paths, String flushType) {
        log.info("【CDN】-purgePathCache：\n{}", paths);
        PurgePathCacheRequest request = new PurgePathCacheRequest();
        request.setPaths(paths.toArray(new String[0]));
        request.setFlushType(flushType);
        try {
            cdnClient.PurgePathCache(request);
        } catch (TencentCloudSDKException e) {
            throw new ApiException("刷新目录异常");
        }
    }

    /**
     * 刷新目录
     *
     * @param path      需要刷新目录
     * @param flushType 刷新类型
     */
    public void purgePathCache(String path, String flushType) {
        this.purgePathCache(Collections.singletonList(path), flushType);
    }

    /**
     * 刷新目录（默认只刷新产生更新的资源）
     *
     * @param path 需要刷新目录
     */
    public void purgePathCache(String path) {
        this.purgePathCache(path, "flush");
    }

    /**
     * 预热URL
     *
     * @param urls 需要预热的URL(完整的http资源路径)
     */
    public void pushUrlsCache(List<String> urls) {
        log.info("【CDN】-pushUrlsCache：\n{}", urls);
        PushUrlsCacheRequest request = new PushUrlsCacheRequest();
        request.setUrls(urls.toArray(new String[0]));
        try {
            cdnClient.PushUrlsCache(request);
        } catch (TencentCloudSDKException e) {
            throw new ApiException("预热URL异常");
        }
    }

    /**
     * 预热URL
     *
     * @param url 需要预热的URL
     */
    public void pushUrlsCache(String url) {
        this.pushUrlsCache(Collections.singletonList(url));
    }


    /**
     * 查询刷新用量配额(包含URL和目录)
     *
     * @return 查询结果
     */
    public DescribePurgeQuotaResponse describePurgeQuota() {
        DescribePurgeQuotaRequest request = new DescribePurgeQuotaRequest();
        try {
            return cdnClient.DescribePurgeQuota(request);
        } catch (TencentCloudSDKException e) {
            throw new ApiException("查询CDN刷新配额异常");
        }
    }

    /**
     * 查询预热用量配额
     *
     * @return 查询结果
     */
    public DescribePushQuotaResponse describePushQuota() {
        DescribePushQuotaRequest request = new DescribePushQuotaRequest();
        try {
            return cdnClient.DescribePushQuota(request);
        } catch (TencentCloudSDKException e) {
            throw new ApiException("查询CDN预热配额异常");
        }
    }

}
