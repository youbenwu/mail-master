package com.ys.mail.service;

import com.qcloud.cos.COSClient;
import com.tencent.cloud.Credentials;
import com.tencent.cloud.Response;
import com.ys.mail.enums.CosFolderEnum;

import java.io.File;
import java.net.URL;
import java.util.TreeMap;

/**
 * @Desc 对象存储OSS服务
 * @Author CRH
 * @Create 2022-01-05 13:23
 */
public interface CosService {

    /**
     * 获取临时凭证
     *
     * @param config 通过永久密钥获取临时凭证
     * @return 临时凭证
     */
    Response getStsCredential(TreeMap<String, Object> config);

    /**
     * 获取OOS客户端
     *
     * @param credentials 凭证，为空则生成永久权限客户端
     * @return client
     */
    COSClient getOssClient(Credentials credentials);

    /**
     * 自定义指定存储桶名称，存储目录
     *
     * @param bucketName 存储桶名称，为空使用默认
     * @param cosFolder  存储目录，为空使用默认
     * @param key        key
     * @param file       文件
     * @return url
     */
    URL upload(String bucketName, CosFolderEnum cosFolder, String key, File file);

    /**
     * 上传到默认桶的默认主目录
     *
     * @param key  key
     * @param file 文件
     * @return url
     */
    URL upload(String key, File file);

    /**
     * 上传到默认桶的指定目录
     *
     * @param cosFolder 指定目录
     * @param key       key
     * @param file      文件
     * @return url
     */
    URL upload(CosFolderEnum cosFolder, String key, File file);

    /**
     * 异步上传
     *
     * @param file      数据文件
     * @param cosFolder 存储目录
     * @param path      路径
     * @return url
     */
    URL asyncUpload(File file, CosFolderEnum cosFolder, String path);

    /**
     * 获取OSS路径
     *
     * @return 路径
     */
    String getOssPath();

    /**
     * 判断key是否存在COS中(默认桶)
     *
     * @param key key
     * @return bool
     */
    Boolean isExistKey(String key);
}
