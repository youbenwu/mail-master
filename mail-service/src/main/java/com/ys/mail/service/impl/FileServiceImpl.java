package com.ys.mail.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReUtil;
import com.ys.mail.constant.StringConstant;
import com.ys.mail.enums.CosFolderEnum;
import com.ys.mail.enums.FilePathEnum;
import com.ys.mail.enums.ImgPathEnum;
import com.ys.mail.enums.RegularEnum;
import com.ys.mail.exception.ApiAssert;
import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.CosService;
import com.ys.mail.service.FileService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.FileTool;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件管理
 *
 * @author CRH
 * @date 2022-04-19 15:19
 * @since 1.0
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private CosService cosService;

    /**
     * 文件限制提示
     */
    private final static String FILE_NULL = "请选择要上传的图片";
    private final static String IMG_FORMAT = "jpg,jpeg,gif,png";
    /**
     * 默认，图片限制500kb
     */
    private final static Long LONG_FILE_MAX_SIZE = 1024 * 500L;

    @Override
    public CommonResult<String> imageUpload(MultipartFile file, ImgPathEnum imgType) {
        // 文件校验
        if (BlankUtil.isEmpty(file)) {
            return CommonResult.failed(FILE_NULL);
        }
        // 根据不同类型校验文件大小
        Long size = imgType.size();
        String sizeName;
        if (BlankUtil.isNotEmpty(size)) {
            sizeName = FileTool.getSize(size);
            ApiAssert.isTrue(file.getSize() > size, BusinessErrorCode.ERR_FILE_SIZE_EXCEED.getMessage(sizeName));
        } else {
            sizeName = FileTool.getSize(LONG_FILE_MAX_SIZE);
            ApiAssert.isTrue(file.getSize() > LONG_FILE_MAX_SIZE, BusinessErrorCode.ERR_FILE_SIZE_EXCEED.getMessage(sizeName));
        }
        // 图片格式校验
        boolean result = IMG_FORMAT.toUpperCase().contains(this.getSuffix(file).toUpperCase());
        if (!result) {
            return CommonResult.failed(String.format("请选择%s格式的图片", IMG_FORMAT));
        }

        // 上传
        String key = imgType.path() + this.getRandomName(file);
        cosService.upload(key, FileTool.multipartToFile(file));
        return CommonResult.success(key);
    }

    @Override
    @SneakyThrows
    public CommonResult<String> asyncFileUpload(MultipartFile file, FilePathEnum filePath, boolean retainName) {
        // 文件校验
        ApiAssert.noValue(file, CommonResultCode.ERR_INTERFACE_PARAM);

        // 文件名处理，默认原名称
        String newFileName = file.getOriginalFilename();
        // 文件名校验（不能包含中文）
        boolean match = ReUtil.isMatch(RegularEnum.FILENAME.getReg(), newFileName);
        ApiAssert.isFalse(match, BusinessErrorCode.ERR_FILE_NAME_FORMAT);
        // 根据存储目录进行文件后缀检测，如：0->apk等
        String suffix = this.getSuffix(file);
        List<String> suffixList = filePath.suffix();
        if (BlankUtil.isNotEmpty(suffixList)) {
            String message = BusinessErrorCode.ERR_FILE_SUFFIX.getMessage(suffixList.toString());
            ApiAssert.isFalse(suffixList.contains(suffix), message);
        }

        // 随机生成
        if (!retainName) {
            newFileName = this.getRandomName(file);
        }
        // 完整的key
        String fullKey = filePath.path() + newFileName;

        // 使用异步上传
        cosService.asyncUpload(FileTool.multipartToFile(file), CosFolderEnum.FILE_FOLDER, fullKey);
        return CommonResult.success(fullKey);
    }

    private String getSuffix(MultipartFile file) {
        String suffix = file.getOriginalFilename();
        if (null == suffix) {
            return StringConstant.BLANK;
        }
        int lastIndexOf = suffix.lastIndexOf(".");
        String message = BusinessErrorCode.ERR_FILE_NOT_SUFFIX.getMessage(file.getName());
        ApiAssert.isTrue(lastIndexOf < 0, message);
        return suffix.substring(lastIndexOf + 1).toLowerCase();
    }

    private String getRandomName(MultipartFile file) {
        return String.format("%s.%s", IdUtil.simpleUUID(), this.getSuffix(file));
    }

}
