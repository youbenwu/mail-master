package com.ys.mail.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.constant.StringConstant;
import com.ys.mail.entity.AmsApp;
import com.ys.mail.enums.CosFolderEnum;
import com.ys.mail.enums.ImgPathEnum;
import com.ys.mail.exception.ApiAssert;
import com.ys.mail.exception.ApiException;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.mapper.AmsAppMapper;
import com.ys.mail.model.admin.param.AmsAppInsertParam;
import com.ys.mail.model.admin.param.AmsAppUpdateParam;
import com.ys.mail.model.admin.query.AppQuery;
import com.ys.mail.model.admin.vo.AmsAppVO;
import com.ys.mail.service.AmsAppService;
import com.ys.mail.service.CosService;
import com.ys.mail.util.*;
import com.ys.mail.wrapper.SqlLambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

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
    private AmsAppMapper amsAppMapper;

    @Override
    public IPage<AmsAppVO> getPage(AppQuery query) {
        IPage<AmsApp> page = new Page<>(query.getPageNum(), query.getPageSize());
        SqlLambdaQueryWrapper<AmsApp> wrapper = new SqlLambdaQueryWrapper<>();
        wrapper.like(AmsApp::getName, query.getName());
        return amsAppMapper.getPage(page, wrapper);
    }

    @Override
    public boolean add(AmsAppInsertParam param) {
        // 名称重复检测
        String name = param.getName();
        boolean existsName = this.isExistsName(name);
        ApiAssert.isTrue(existsName, CommonResultCode.ERR_PARAM_EXIST.getMessage(name));

        String qrcodeName = param.getQrcodeName();
        existsName = this.isExistsQrcodeName(qrcodeName);
        ApiAssert.isTrue(existsName, CommonResultCode.ERR_PARAM_EXIST.getMessage(qrcodeName));
        try {
            // 二维码内容
            String content = String.format("%s%s?%s", cosService.getOssPath(), param.getUrl(), RandomUtil.randomString(32));
            String key = this.genQrCode(content, qrcodeName, param.getUseLogo(), param.getLogoType());

            // 构建插入对象
            AmsApp amsApp = new AmsApp();
            BeanUtils.copyProperties(param, amsApp);
            amsApp.setId(IdWorker.generateId());
            amsApp.setPcUserId(PcUserUtil.getCurrentUser().getPcUserId());
            // 填充二维码地址
            amsApp.setQrcodeUrl(key);

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
        String qrcodeName = param.getQrcodeName();
        String url = param.getUrl();
        String qrcodeUrl = amsApp.getQrcodeUrl();
        boolean existsName;
        if (!amsApp.getName().equals(name)) {
            existsName = this.isExistsName(name);
            ApiAssert.isTrue(existsName, CommonResultCode.ERR_PARAM_EXIST.getMessage(name));
        }
        if (!amsApp.getQrcodeName().equals(qrcodeName)) {
            existsName = this.isExistsQrcodeName(qrcodeName);
            ApiAssert.isTrue(existsName, CommonResultCode.ERR_PARAM_EXIST.getMessage(qrcodeName));
        }

        try {
            // 如果APP链接变更，则删除文件
            boolean urlUpdated = !amsApp.getUrl().equals(url);
            if (urlUpdated) {
                // 删除APP文件
                cosService.deleteObject(CosFolderEnum.FILE_FOLDER, url);
            }

            // 当二维码名称或者APP变更时，重新生成二维码
            if (!amsApp.getQrcodeName().equals(param.getQrcodeName()) || urlUpdated) {
                // 删除下载二维码
                cosService.deleteObject(qrcodeUrl);
                // 二维码内容
                String content = String.format("%s%s?%s", cosService.getOssPath(), url, RandomUtil.randomString(32));
                // 开始上传
                String key = this.genQrCode(content, qrcodeName, param.getUseLogo(), param.getLogoType());
                // 填充二维码地址
                amsApp.setQrcodeUrl(key);
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
        SqlLambdaQueryWrapper<AmsApp> wrapper = new SqlLambdaQueryWrapper<>();
        wrapper.eq(AmsApp::getName, name)
               .last(StringConstant.LIMIT_ONE);
        AmsApp amsApp = this.getOne(wrapper);
        return BlankUtil.isNotEmpty(amsApp);
    }

    @Override
    public boolean isExistsQrcodeName(String qrcodeName) {
        SqlLambdaQueryWrapper<AmsApp> wrapper = new SqlLambdaQueryWrapper<>();
        wrapper.eq(AmsApp::getQrcodeName, qrcodeName)
               .last(StringConstant.LIMIT_ONE);
        AmsApp amsApp = this.getOne(wrapper);
        return BlankUtil.isNotEmpty(amsApp);
    }

    @Override
    public String genQrCode(String content, String qrcodeName, boolean useLogo, Integer logoType) throws Exception {
        // 二维码临时文件
        File qrcode = File.createTempFile("temp-qrcode-", ".png");
        // 生成二维码
        if (useLogo) {
            // 获取Logo文件
            File logoFile = this.getAppLogo(logoType);
            QrCodeUtil.encode(content, logoFile, qrcode);
        } else {
            QrCodeUtil.encode(content, qrcode);
        }
        // 构建二维码上传key
        String key = ImgPathEnum.DOWNLOAD_QRCODE_PATH.value() + qrcodeName + ".png";
        // 上传到指定目录
        cosService.upload(key, qrcode);
        // 返回Key
        return key;
    }

    @Override
    public boolean delete(Long id) {
        // 获取记录
        AmsApp amsApp = this.getById(id);
        ApiAssert.noValue(amsApp, CommonResultCode.ID_NO_EXIST);
        // 删除数据库记录
        boolean result = this.removeById(id);
        // 删除文件
        this.deleteFile(amsApp.getUrl(), amsApp.getQrcodeUrl());
        return result;
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
        cosService.download(CosFolderEnum.IMAGES_FOLDER, String.format("%s%s%d.png", ImgPathEnum.SYS_LOGO_PATH.value(), "app", type), logo);
        return logo;
    }

}
