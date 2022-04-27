package com.ys.mail.service;

import com.ys.mail.enums.FilePathEnum;
import com.ys.mail.enums.ImgPathEnum;
import com.ys.mail.model.CommonResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Desc 文件管理接口
 * @Author CRH
 * @Create 2022-01-24 13:12
 */
public interface FileService {

    /**
     * 图片上传，有大小类型限制(腾讯COS)
     *
     * @param file    文件
     * @param imgType 图片类型，不同类型存储的文件夹不同
     * @return 上传结果
     */
    CommonResult<String> imageUpload(MultipartFile file, ImgPathEnum imgType);

    /**
     * 异步文件上传，大小类型宽松处理
     *
     * @param file     文件
     * @param filePath 文件类型
     * @param filename 文件名称，为空则随机生成
     * @return 结果
     */
    CommonResult<String> asyncFileUpload(MultipartFile file, FilePathEnum filePath, String filename);
}
