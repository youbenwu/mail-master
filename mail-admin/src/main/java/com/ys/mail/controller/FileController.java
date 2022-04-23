package com.ys.mail.controller;


import com.ys.mail.annotation.EnumContains;
import com.ys.mail.enums.EnumFilePath;
import com.ys.mail.enums.EnumImgPath;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.CosService;
import com.ys.mail.service.FileService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.EnumTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cjp
 * @date 2020/5/7
 * 文件上传
 */
@Validated
@RestController
@Api(tags = "文件管理")
@RequestMapping("/pc/file")
public class FileController {

    private final static Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;
    @Autowired
    private CosService cosService;

    @ApiOperation("获取COS的图片最新存储路径")
    @GetMapping(value = "/getCOSPath")
    public CommonResult<String> getOssPath() {
        String cosPath = cosService.getOssPath();
        return CommonResult.success(cosPath);
    }

    @ApiOperation(value = "图片上传-COS", notes = "上传到腾讯云COS，图片大小不能超过500kb")
    @PostMapping(value = "/cos/upload")
    @ApiImplicitParam(name = "imgType", value = "图片类型：-1->开发测试,0->系统设置,1->商品图片,2->用户头像,3->二维码,4->商品评价,5->身份证,6->轮播图,7->融云聊天,8->企业品牌", required = true, dataType = "int")
    public CommonResult<String> cosUpload(@RequestParam(name = "file") MultipartFile file,
                                          @RequestParam(name = "imgType") @EnumContains(enumClass = EnumImgPath.class) Integer imgType,
                                          HttpServletRequest req) {
        return fileService.imageUpload(file, EnumTool.getEnum(EnumImgPath.class, imgType));
    }

    @ApiOperation(value = "图片批量上传-COS", notes = "批量上传图片到腾讯云COS")
    @PostMapping(value = "/cos/uploadBatch")
    @ApiImplicitParam(name = "imgType", value = "图片类型：-1->开发测试,0->系统设置,1->商品图片,2->用户头像,3->二维码,4->商品评价,5->身份证,6->轮播图,7->融云聊天,8->企业品牌", required = true, dataType = "int")
    public CommonResult<String> cosUploadBatch(@RequestParam(value = "file", required = false) MultipartFile[] multipartFile,
                                               @RequestParam(name = "imgType") @EnumContains(enumClass = EnumImgPath.class) Integer imgType,
                                               HttpServletRequest req) {
        if (BlankUtil.isEmpty(multipartFile)) {
            return CommonResult.failed("请选择要上传的图片");
        }
        List<String> imgList = new ArrayList<>();
        String join = "";
        if (multipartFile.length > 0) {
            for (MultipartFile mf : multipartFile) {
                CommonResult<String> result = fileService.imageUpload(mf, EnumTool.getEnum(EnumImgPath.class, imgType));
                if (result.getCode() != CommonResultCode.SUCCESS.getCode()) return result;
                imgList.add(result.getData());
                join = String.join(";", imgList);
            }
            return CommonResult.success("图片批量上传成功", join);
        }
        return CommonResult.failed("图片批量上传失败");
    }

    @PostMapping(value = "/cos/async/fileUpload")
    @ApiOperation(value = "异步文件上传-COS", notes = "最大不超过200MB")
    @ApiImplicitParam(name = "fileType", value = "文件类型：-1->开发测试,0->APK目录,1->视频存储", required = true, dataType = "int")
    public CommonResult<String> asyncFileUpload(@RequestParam(name = "file") MultipartFile multipartFile,
                                                @RequestParam(name = "fileType") @EnumContains(enumClass = EnumFilePath.class) Integer fileType) {
        return fileService.asyncFileUpload(multipartFile, EnumTool.getEnum(EnumFilePath.class, fileType), "");
    }
}
