package com.ys.mail.util;

import cn.hutool.core.io.FileUtil;
import com.ys.mail.exception.ApiException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * @Desc File工具类，扩展 hutool 工具包
 * @Author CRH
 * @Create 2022-03-07 16:27
 */
public class FileTool extends FileUtil {

    /**
     * 格式化小数
     */
    private static final DecimalFormat DF = new DecimalFormat("0.00");

    /**
     * 定义GB的计算常量
     */
    private static final int GB = 1024 * 1024 * 1024;
    /**
     * 定义MB的计算常量
     */
    private static final int MB = 1024 * 1024;
    /**
     * 定义KB的计算常量
     */
    private static final int KB = 1024;

    /**
     * 文件大小转换
     */
    public static String getSize(long size) {
        String resultSize;
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = DF.format(size / (float) GB) + "GB";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = DF.format(size / (float) MB) + "MB";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = DF.format(size / (float) KB) + "KB";
        } else {
            resultSize = size + "B";
        }
        return resultSize;
    }

    /**
     * 将上传的文件转换为普通文件
     *
     * @param multipartFile 上传的文件
     * @return 文件
     */
    public static File multipartToFile(MultipartFile multipartFile) {
        File localFile;
        try {
            localFile = File.createTempFile("temp_upload_", null);
            multipartFile.transferTo(localFile);
        } catch (IOException e) {
            throw new ApiException("文件转换异常：" + multipartFile.getName());
        }
        return localFile;
    }

    /**
     * 判断路径是否存在
     *
     * @param path 路径
     * @return 是否存在
     */
    public static boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }

}
