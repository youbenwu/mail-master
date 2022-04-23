package com.ys.mail.service.impl;

import cn.hutool.core.util.IdUtil;
import com.ys.mail.enums.EnumCosFolder;
import com.ys.mail.enums.EnumFilePath;
import com.ys.mail.enums.EnumImgPath;
import com.ys.mail.exception.ApiException;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.CosService;
import com.ys.mail.service.FileService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.FileTool;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Desc 文件管理
 * @Author CRH
 * @Create 2022-01-24 13:16
 */
@Service
public class FileServiceImpl implements FileService {

    private final static Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private CosService cosService;

    /**
     * 文件限制提示
     */
    private final static String FILE_NULL = "请选择要上传的图片";
    private final static String IMG_FORMAT = "jpg,jpeg,gif,png";
    /**
     * 图片限制500kb
     */
    private final static Long LONG_FILE_MAX_SIZE = 1024 * 500L;
    private final static String FILE_SIZE_OVERSTEP = "文件大小不能大于500KB";

    @Override
    public CommonResult<String> imageUpload(MultipartFile file, EnumImgPath imgType) {
        // 文件校验
        if (BlankUtil.isEmpty(file)) {
            return CommonResult.failed(FILE_NULL);
        }
        if (file.getSize() > LONG_FILE_MAX_SIZE) {
            return CommonResult.failed(FILE_SIZE_OVERSTEP);
        }
        // 图片格式校验
        boolean b = IMG_FORMAT.toUpperCase().contains(this.getSuffix(file).toUpperCase());
        if (!b) return CommonResult.failed(String.format("请选择%s格式的图片", IMG_FORMAT));

        // 上传
        String key = imgType.value() + this.getRandomName(file);
        cosService.upload(key, FileTool.multipartToFile(file));
        return CommonResult.success(key);
    }

    @Override
    @SneakyThrows
    public CommonResult<String> asyncFileUpload(MultipartFile file, EnumFilePath filePath, String filename) {
        // 文件校验
        if (BlankUtil.isEmpty(file)) return CommonResult.failed(CommonResultCode.ERR_INTERFACE_PARAM);
        // 文件名校验
        String newFileName = this.getRandomName(file);
        if (BlankUtil.isNotEmpty(filename)) {
            if (filename.contains(".")) newFileName = filename;
            else newFileName = filename + this.getSuffix(file);

        }
        // 使用异步上传
        String path = filePath.value() + newFileName;
        cosService.asyncUpload(FileTool.multipartToFile(file), EnumCosFolder.FILE_FOLDER, path);
        return CommonResult.success(path);
    }

    private String getSuffix(MultipartFile file) {
        String suffix = file.getOriginalFilename();
        if (BlankUtil.isEmpty(suffix)) throw new ApiException(String.format("请上传带后缀的文件：%s", file.getName()));
        return suffix.substring(suffix.lastIndexOf(".") + 1);
    }

    private String getRandomName(MultipartFile file) {
        return String.format("%s.%s", IdUtil.simpleUUID(), this.getSuffix(file));
    }

}
