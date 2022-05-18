package com.ys.mail.service;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.transfer.TransferManager;
import com.tencent.cloud.Credentials;
import com.tencent.cloud.Response;
import com.ys.mail.constant.WarningsConstant;
import com.ys.mail.enums.CosFolderEnum;

import java.io.File;
import java.net.URL;
import java.util.TreeMap;

/**
 * --
 *
 * @author 对象存储OSS服务
 * @date 2022-04-19 15:19
 * @since 1.0
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
    @SuppressWarnings(WarningsConstant.UNUSED)
    URL asyncUpload(File file, CosFolderEnum cosFolder, String path);

    /**
     * 下载文件到本地
     *
     * @param bucketName 存储桶
     * @param cosFolder  文件夹
     * @param key        文件key
     * @param localFile  本地空文件
     * @param cover      是否强制覆盖本地
     */
    void download(String bucketName, CosFolderEnum cosFolder, String key, File localFile, boolean cover);

    /**
     * 下载文件到本地
     *
     * @param cosFolder 文件夹
     * @param key       文件key
     * @param localFile 本地空文件
     * @param cover     是否强制覆盖本地
     */
    void download(CosFolderEnum cosFolder, String key, File localFile, boolean cover);

    /**
     * 下载文件到本地，如果本地存在则直接返回
     *
     * @param cosFolder 文件夹
     * @param key       文件key
     * @param localFile 本地空文件
     */
    void download(CosFolderEnum cosFolder, String key, File localFile);

    /**
     * 获取完整的key
     *
     * @param cosFolder 目录
     * @param key       文件key
     * @return 返回完整的key
     */
    String getFullKey(CosFolderEnum cosFolder, String key);

    /**
     * 获取传输管理器
     *
     * @return 传输管理器对象
     */
    TransferManager getTransferManager();

    /**
     * 获取客户端
     *
     * @return 客户端
     */
    COSClient getCOSClient();

    /**
     * 删除指定的文件
     *
     * @param cosFolder 文件目录
     * @param key       文件key
     */
    void deleteObject(CosFolderEnum cosFolder, String key);

    /**
     * 删除默认文件目录中的文件
     *
     * @param key 文件Key
     */
    void deleteObject(String key);
}
