package com.ys.mail.controller;


import com.ys.mail.annotation.EnumDocumentValid;
import com.ys.mail.enums.ImgPathEnum;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.CosService;
import com.ys.mail.service.FileService;
import com.ys.mail.util.EnumTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author cjp
 * @date 2020/5/7
 * 文件上传
 */
@Validated
@RestController
@Api(tags = "文件管理")
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;
    @Autowired
    private CosService cosService;

    @ApiOperation("获取COS的图片最新存储路径")
    @GetMapping(value = "/getCOSPath")
    public CommonResult<String> getCosPath() {
        String cosPath = cosService.getOssPath();
        return CommonResult.success(cosPath);
    }

    @ApiOperation(value = "图片上传-COS", notes = "上传到腾讯云COS<br/>" +
            "使用参考：https://cloud.tencent.com/document/product/460/36540 如下：<br/>" +
            "1、直接使用原图：不带参数即可<br/>" +
            "2、图片等比例缩放：?imageMogr2/thumbnail/100x<br/>" +
            "3、图片为原图50%：?imageMogr2/thumbnail/!50p 等<br/>")
    @PostMapping(value = "/cos/upload")
    @ApiImplicitParam(name = "imgType", value = "图片类型", required = true, dataType = "int")
    public CommonResult<String> cosUpload(@RequestParam(name = "file") MultipartFile file,
                                          @RequestParam(name = "imgType")
                                          @EnumDocumentValid(enumClass = ImgPathEnum.class, exclude = {-3, -2, 0, 3, 6, 8, 10}) Integer imgType) {
        return fileService.imageUpload(file, EnumTool.getEnum(ImgPathEnum.class, imgType));
    }

}
