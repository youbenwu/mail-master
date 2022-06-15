package com.ys.mail.controller;


import com.ys.mail.annotation.EnumDocumentValid;
import com.ys.mail.enums.FilePathEnum;
import com.ys.mail.enums.ImgPathEnum;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.CosService;
import com.ys.mail.service.FileService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.EnumTool;
import com.ys.mail.util.RequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @PostMapping(value = "/cos/upload")
    @ApiOperation(value = "图片上传-COS", notes = "上传到腾讯云COS，不同类型图片大小不一致")
    @ApiImplicitParam(name = "imgType", value = "图片类型", required = true, dataType = "int")
    public CommonResult<String> cosUpload(@RequestParam(name = "file") MultipartFile file,
                                          @RequestParam(name = "imgType") @EnumDocumentValid(enumClass = ImgPathEnum.class, exclude = {-3, -2}) Integer imgType,
                                          HttpServletRequest req, HttpServletResponse res) {
        CommonResult<String> result = fileService.imageUpload(file, EnumTool.getEnum(ImgPathEnum.class, imgType));
        RequestUtil.setStatus(res, result);
        return result;
    }

    @PostMapping(value = "/cos/uploadBatch")
    @ApiOperation(value = "图片批量上传-COS", notes = "批量上传图片到腾讯云COS")
    @ApiImplicitParam(name = "imgType", value = "图片类型", required = true, dataType = "int")
    public CommonResult<String> cosUploadBatch(@RequestParam(value = "file", required = false) MultipartFile[] multipartFile,
                                               @RequestParam(name = "imgType") @EnumDocumentValid(enumClass = ImgPathEnum.class, exclude = {-3, -2}) Integer imgType,
                                               HttpServletRequest req, HttpServletResponse res) {
        if (BlankUtil.isEmpty(multipartFile)) {
            return CommonResult.failed("请选择要上传的图片");
        }
        List<String> imgList = new ArrayList<>();
        String join = "";
        if (multipartFile.length > 0) {
            for (MultipartFile mf : multipartFile) {
                CommonResult<String> result = fileService.imageUpload(mf, EnumTool.getEnum(ImgPathEnum.class, imgType));
                if (result.getCode() != CommonResultCode.SUCCESS.getCode()) return result;
                imgList.add(result.getData());
                join = String.join(";", imgList);
            }
            return CommonResult.success("图片批量上传成功", join);
        }
        return CommonResult.failed("图片批量上传失败");
    }

    @ResponseBody
    @PostMapping(value = "/cos/async/fileUpload")
    @ApiOperation(value = "异步文件上传-COS", notes = "最大不超过200MB")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileType", value = "文件类型", required = true, dataType = "int"),
            @ApiImplicitParam(name = "retainName", value = "是否保留原文件名(注意，文件名不能带中文)，默认为false->随机生成，true->保留", dataType = "boolean")
    })
    public CommonResult<String> asyncFileUpload(@RequestParam(name = "file") MultipartFile multipartFile, @RequestParam(name = "fileType") @EnumDocumentValid(enumClass = FilePathEnum.class) Integer fileType, @RequestParam(name = "retainName", defaultValue = "0") boolean retainName) {
        return fileService.asyncFileUpload(multipartFile, EnumTool.getEnum(FilePathEnum.class, fileType), retainName);
    }
}
