package com.ys.mail.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcloud.cos.model.ObjectMetadata;
import com.tencentcloudapi.cdn.v20180606.models.DescribePurgeQuotaResponse;
import com.tencentcloudapi.cdn.v20180606.models.DescribePushQuotaResponse;
import com.tencentcloudapi.cdn.v20180606.models.Quota;
import com.ys.mail.component.CdnService;
import com.ys.mail.config.CosConfig;
import com.ys.mail.constant.NumberConstant;
import com.ys.mail.constant.StringConstant;
import com.ys.mail.entity.AmsApp;
import com.ys.mail.entity.SysSetting;
import com.ys.mail.enums.CosFolderEnum;
import com.ys.mail.enums.ImgPathEnum;
import com.ys.mail.enums.SettingTypeEnum;
import com.ys.mail.exception.ApiAssert;
import com.ys.mail.exception.ApiException;
import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.mapper.AmsAppMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.dto.AppReleaseInfoDTO;
import com.ys.mail.model.admin.param.AmsAppInsertParam;
import com.ys.mail.model.admin.param.AmsAppUpdateParam;
import com.ys.mail.model.admin.param.SysSettingParam;
import com.ys.mail.model.admin.query.AppQuery;
import com.ys.mail.model.admin.vo.AmsAppVO;
import com.ys.mail.service.AmsAppService;
import com.ys.mail.service.CosService;
import com.ys.mail.service.SysSettingService;
import com.ys.mail.util.*;
import com.ys.mail.wrapper.SqlLambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 070
 * @since 2022-05-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AmsAppServiceImpl extends ServiceImpl<AmsAppMapper, AmsApp> implements AmsAppService {

    @Autowired
    private CosService cosService;
    @Autowired
    private CosConfig cosConfig;
    @Autowired
    private AmsAppMapper amsAppMapper;
    @Autowired
    private SysSettingService sysSettingService;
    @Autowired
    private CdnService cdnService;

    @Override
    public IPage<AmsAppVO> getPage(AppQuery query) {
        IPage<AmsApp> page = new Page<>(query.getPageNum(), query.getPageSize());
        SqlLambdaQueryWrapper<AmsApp> wrapper = new SqlLambdaQueryWrapper<>();
        wrapper.like(AmsApp::getName, query.getName())
               .eq(AmsApp::getUploadStatus, query.getUploadStatus());
        IPage<AmsAppVO> result = amsAppMapper.getPage(page, wrapper);
        // 添加额外项
        result.getRecords().forEach(app -> app.setAppLogoUrl(StringConstant.SLASH + CosFolderEnum.IMAGES_FOLDER.value() + this.getAppLogoPath(app.getType())));
        return result;
    }

    @Override
    public boolean add(AmsAppInsertParam param) {
        // 名称重复检测
        String name = param.getName();
        boolean existsName = this.isExistsName(name);
        ApiAssert.isTrue(existsName, CommonResultCode.ERR_PARAM_EXIST.getMessage(name));

        try {
            // 构建插入对象
            AmsApp amsApp = new AmsApp();
            BeanUtils.copyProperties(param, amsApp);
            amsApp.setId(IdWorker.generateId());
            amsApp.setPcUserId(PcUserUtil.getCurrentUser().getPcUserId());

            // 更新记录到数据库中
            return this.save(amsApp);
        } catch (Exception e) {
            throw new ApiException("添加APP应用失败");
        }
    }

    @Override
    public boolean update(AmsAppUpdateParam param) {
        // ID查询
        AmsApp amsApp = this.getById(param.getId());
        ApiAssert.noValue(amsApp, CommonResultCode.ID_NO_EXIST);
        // 名称重复检测：当新名称与旧名称不一致时需要检测
        String name = param.getName();
        String url = param.getUrl();
        Integer type = param.getType();

        if (!amsApp.getName().equals(name)) {
            boolean existsName = this.isExistsName(name);
            ApiAssert.isTrue(existsName, CommonResultCode.ERR_PARAM_EXIST.getMessage(name));
        }

        try {
            // 如果APP链接或平台类型变更时，则删除文件
            boolean urlUpdated = !amsApp.getUrl().equals(url) || !amsApp.getType().equals(type);
            if (urlUpdated) {
                // 重置状态
                amsApp.setUploadStatus(NumberConstant.ZERO);
                amsApp.setSize(Long.valueOf(NumberConstant.ZERO));
                amsApp.setReleased(Boolean.FALSE);
                amsApp.setQrcodeUrl(StringConstant.BLANK);
            }

            // 构建更新对象
            BeanUtils.copyProperties(param, amsApp);
            amsApp.setPcUserId(PcUserUtil.getCurrentUser().getPcUserId());
            // 更新信息到数据库
            return this.updateById(amsApp);
        } catch (Exception e) {
            throw new ApiException("更新APP应用失败");
        }
    }

    @Override
    public boolean isExistsName(String name) {
        if (BlankUtil.isEmpty(name)) {
            return false;
        }
        SqlLambdaQueryWrapper<AmsApp> wrapper = new SqlLambdaQueryWrapper<>();
        wrapper.eq(AmsApp::getName, name)
               .last(StringConstant.LIMIT_ONE);
        AmsApp amsApp = this.getOne(wrapper);
        return BlankUtil.isNotEmpty(amsApp);
    }

    @Override
    public boolean delete(Long id) {
        // 获取记录
        AmsApp amsApp = this.getById(id);
        ApiAssert.noValue(amsApp, CommonResultCode.ID_NO_EXIST);
        // 删除数据库记录
        return this.removeById(id);
    }

    @Override
    public boolean check(Long id) {
        // 获取记录
        AmsApp amsApp = this.getById(id);
        ApiAssert.noValue(amsApp, CommonResultCode.ID_NO_EXIST);
        // 获取文件信息
        ObjectMetadata objectInfo = cosService.getObjectInfo(CosFolderEnum.FILE_FOLDER, amsApp.getUrl());
        boolean needUpdate = true;
        Long size = amsApp.getSize();
        Integer uploadStatus = amsApp.getUploadStatus();
        if (BlankUtil.isNotEmpty(objectInfo)) {
            // 设置大小
            long contentLength = objectInfo.getContentLength();
            // 当文件内容长度为零时，并且上传状态为已上传 或者 内容长度不变时，表示已经更新过了，那么将会跳过更新
            boolean condition = contentLength == NumberUtils.LONG_ZERO && uploadStatus.equals(NumberConstant.ONE) || contentLength == size;
            if (condition) {
                needUpdate = false;
            } else {
                amsApp.setSize(contentLength);
                amsApp.setUploadStatus(NumberConstant.ONE);
            }
        } else {
            // 当数据已经更新过了，那么将会跳过更新
            if (size.equals(NumberUtils.LONG_ZERO) && uploadStatus.equals(NumberConstant.ZERO)) {
                needUpdate = false;
            } else {
                amsApp.setSize(Long.valueOf(NumberConstant.ZERO));
                amsApp.setUploadStatus(NumberConstant.ZERO);
            }
        }
        // 更新到数据库中
        if (needUpdate) {
            return this.updateById(amsApp);
        }
        return Boolean.TRUE;
    }

    @Override
    public boolean genQrcode(Long id) {
        // 获取记录
        AmsApp amsApp = this.getById(id);
        ApiAssert.noValue(amsApp, CommonResultCode.ID_NO_EXIST);
        // 检测APP上传状态，只有为已上传才可以进行发布
        Integer uploadStatus = amsApp.getUploadStatus();
        ApiAssert.isFalse(uploadStatus.equals(NumberConstant.ONE), BusinessErrorCode.UNFINISHED_APP_UPLOAD);

        // 获取应用信息
        String url = amsApp.getUrl();
        Boolean useLogo = amsApp.getUseLogo();
        Integer type = amsApp.getType();
        // 二维码内容
        String content = String.format("%s%s?", cosService.getOssPath(CosFolderEnum.FILE_FOLDER), url);
        content = this.paddingUrl(content);
        try {
            // 开始上传
            String key = this.uploadQrCode(content, useLogo, type);
            // 填充二维码地址
            amsApp.setQrcodeUrl(key);
            // 将当前类型APP的二维码置空
            this.resetQrcodeUrl(type);
            // 更新到数据库中
            return this.updateById(amsApp);
        } catch (Exception e) {
            throw new ApiException("生成二维码失败");
        }
    }

    @Override
    public CommonResult<Boolean> release(Long id) {
        // 获取记录
        AmsApp amsApp = this.getById(id);
        ApiAssert.noValue(amsApp, CommonResultCode.ID_NO_EXIST);
        // 检测APP上传状态，只有为已上传才可以进行发布
        Integer uploadStatus = amsApp.getUploadStatus();
        ApiAssert.isFalse(uploadStatus.equals(NumberConstant.ONE), BusinessErrorCode.UNFINISHED_APP_UPLOAD);
        // 校验APP是否已生成二维码
        String qrcodeUrl = amsApp.getQrcodeUrl();
        ApiAssert.noValue(qrcodeUrl, BusinessErrorCode.APP_NOT_GENERATE_QRCODE);

        // 根据类型获取设置信息
        Integer type = amsApp.getType();
        Integer appType;
        if (type.equals(NumberConstant.ZERO)) {
            appType = SettingTypeEnum.twenty_seven.key();
        } else {
            appType = SettingTypeEnum.twenty_eight.key();
        }
        SettingTypeEnum typeEnum = EnumTool.getEnum(SettingTypeEnum.class, appType);
        SysSetting setting = sysSettingService.getOneByType(typeEnum);
        ApiAssert.noValue(setting, BusinessErrorCode.ERR_SETTING_TYPE_NOT_EXIST);

        // 构建发布信息
        AppReleaseInfoDTO build = AppReleaseInfoDTO.builder()
                                                   .id(amsApp.getId())
                                                   .appName(amsApp.getName())
                                                   .versionCode(Integer.valueOf(amsApp.getVersionCode()))
                                                   .versionName(amsApp.getVersionName())
                                                   .fullQrcodeUrl(cosService.getOssPath() + qrcodeUrl)
                                                   .updateTitle(amsApp.getUpdateTitle())
                                                   .updateContent(amsApp.getUpdateContent())
                                                   .forcedUpdate(amsApp.getForcedUpdate())
                                                   .downloadUrl(cosService.getOssPath(CosFolderEnum.FILE_FOLDER) + amsApp.getUrl())
                                                   .packageSize(amsApp.getSize())
                                                   .updateTime(amsApp.getUpdateTime()).build();


        // 更新应用信息
        this.resetReleased(type);
        amsApp.setReleased(Boolean.TRUE);
        this.updateById(amsApp);

        // 更新设置
        setting.setSettingValue(JSONObject.toJSONString(build));
        SysSettingParam param = new SysSettingParam();
        BeanUtils.copyProperties(setting, param);
        param.setSysSettingId(String.valueOf(setting.getSysSettingId()));
        param.setSettingType(String.valueOf(setting.getSettingType()));
        return sysSettingService.addOrUpdate(param, PcUserUtil.getCurrentUser().getPcUserId());
    }

    @Override
    public String purgeAndWarmUp(Long id) {
        // 获取记录
        AmsApp amsApp = this.getById(id);
        ApiAssert.noValue(amsApp, CommonResultCode.ID_NO_EXIST);

        // 检测APP状态（当状态为已上传才允许发布）
        ApiAssert.isFalse(amsApp.getUploadStatus().equals(NumberConstant.ONE), BusinessErrorCode.UNFINISHED_APP_UPLOAD);
        // 校验APP是否已生成二维码
        String qrcodeUrl = amsApp.getQrcodeUrl();
        ApiAssert.noValue(qrcodeUrl, BusinessErrorCode.APP_NOT_GENERATE_QRCODE);

        // 校验每日刷新用量配额(中国境内)
        DescribePurgeQuotaResponse dpqResponse = cdnService.describePurgeQuota();
        Quota[] urlPurge = dpqResponse.getUrlPurge();
        Long availableUrlPurgeNumber = urlPurge[0].getAvailable();
        ApiAssert.isTrue(availableUrlPurgeNumber == 0, BusinessErrorCode.CDN_URL_PURGE_QUOTA_EXCEED);

        // 刷新二维码
        String fullQrcodeUrl = cosService.getOssPath() + qrcodeUrl;
        cdnService.purgeUrlsCache(fullQrcodeUrl);

        // 校验每日预热用量配额
        DescribePushQuotaResponse dpqResponse2 = cdnService.describePushQuota();
        Quota[] urlPush = dpqResponse2.getUrlPush();
        Long availableUrlPushNumber = urlPush[0].getAvailable();
        ApiAssert.isTrue(availableUrlPushNumber == 0, BusinessErrorCode.CDN_URL_PUSH_QUOTA_EXCEED);

        // 只预热APP
        String fullAppUrl = cosService.getOssPath(CosFolderEnum.FILE_FOLDER) + amsApp.getUrl();
        cdnService.pushUrlsCache(fullAppUrl);

        // 返回结果
        return String.format("执行成功，当前URL刷新剩余%s条，URL预热剩余%s条", availableUrlPurgeNumber - 1, availableUrlPushNumber - 1);
    }

    @Override
    public Map<String, String> qrcodeInfo() {
        Map<String, String> map = new LinkedHashMap<>();
        // 获取设置信息
        JSON updateInfo = sysSettingService.getSettingValue(SettingTypeEnum.twenty_seven);

        // 解析内容
        JSONObject jsonObject;
        if (BlankUtil.isNotEmpty(updateInfo)) {
            jsonObject = JSONObject.parseObject(updateInfo.toString());
            String fullQrcodeUrl = String.valueOf(jsonObject.get("fullQrcodeUrl"));
            map.put("appZero", fullQrcodeUrl);
        }
        updateInfo = sysSettingService.getSettingValue(SettingTypeEnum.twenty_eight);
        if (BlankUtil.isNotEmpty(updateInfo)) {
            jsonObject = JSONObject.parseObject(updateInfo.toString());
            String fullQrcodeUrl = String.valueOf(jsonObject.get("fullQrcodeUrl"));
            map.put("appOne", fullQrcodeUrl);
        }

        // 返回数据
        return map;
    }

    @Override
    public String getAppLogoPath(Integer type) {
        return String.format("%s%s%d.png", ImgPathEnum.SYS_LOGO_PATH.value(), "app", type);
    }

    /**
     * 生成并上传二维码
     *
     * @param content 二维码内容
     * @param useLogo 是否使用Logo
     * @param type    APP类型
     * @return 二维码的存储key
     * @throws Exception e
     */
    private String uploadQrCode(String content, boolean useLogo, Integer type) throws Exception {
        // 二维码临时文件
        File qrcode = File.createTempFile("temp-qrcode-", ".png");
        // 生成二维码
        if (useLogo) {
            // 获取Logo文件
            File logoFile = this.getAppLogo(type);
            QrCodeUtil.encode(content, logoFile, qrcode);
        } else {
            QrCodeUtil.encode(content, qrcode);
        }
        // 构建二维码上传key
        String key = ImgPathEnum.DOWNLOAD_QRCODE_PATH.value() + genQrcodeName(type);
        // 上传到指定目录
        cosService.upload(key, qrcode);
        // 返回Key
        return key;
    }

    /**
     * 根据类型将发布状态置为0
     *
     * @param type APP类型
     */
    private void resetReleased(Integer type) {
        LambdaUpdateWrapper<AmsApp> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(AmsApp::getReleased, NumberConstant.ZERO)
               .eq(AmsApp::getType, type);
        this.update(wrapper);
    }

    /**
     * 根据类型置空二维码
     *
     * @param type APP类型
     */
    private void resetQrcodeUrl(Integer type) {
        LambdaUpdateWrapper<AmsApp> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(AmsApp::getQrcodeUrl, StringConstant.BLANK)
               .eq(AmsApp::getType, type);
        this.update(wrapper);
    }

    /**
     * 删除应用和文件
     *
     * @param url       应用Key
     * @param qrcodeUrl 二维码Key
     */
    private void deleteFile(String url, String qrcodeUrl) {
        // 删除APP文件
        cosService.deleteObject(CosFolderEnum.FILE_FOLDER, url);
        // 删除下载二维码
        cosService.deleteObject(qrcodeUrl);
    }

    /**
     * 获取APP的Logo图片，当本地不存在时将从cos进行下载
     *
     * @param type app类型，0->app0，1->app1
     * @return Logo文件
     */
    private File getAppLogo(Integer type) {
        String fullPath = StrUtil.format("{}temp-logo-app{}.png", SystemUtil.getTmpDir(), type);
        File logo = new File(fullPath);
        cosService.download(CosFolderEnum.IMAGES_FOLDER, this.getAppLogoPath(type), logo);
        return logo;
    }

    private String paddingUrl(String content) {
        int length = content.length();
        int result = NumberConstant.HUNDRED_TWENTY - length;
        if (result > 0) {
            content += RandomUtil.randomString(result);
        }
        return content;
    }

    private String genQrcodeName(Integer type) {
        String result;
        if (BlankUtil.isNotEmpty(type) && type.equals(NumberConstant.ZERO)) {
            result = StringConstant.APP_ZERO;
        } else {
            result = StringConstant.APP_ONE;
        }
        return result + StringConstant.PNG;
    }

}
