package com.ys.mail.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.config.RedisConfig;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.entity.UmsUserInvite;
import com.ys.mail.enums.ImgPathEnum;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.mapper.UmsUserInviteMapper;
import com.ys.mail.mapper.UmsUserMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.CosService;
import com.ys.mail.service.RedisService;
import com.ys.mail.service.UmsUserInviteService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.QRCodeUtil;
import com.ys.mail.util.UserUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;

/**
 * <p>
 * 用户邀请信息表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-22
 */

@Service
public class UmsUserInviteServiceImpl extends ServiceImpl<UmsUserInviteMapper, UmsUserInvite> implements UmsUserInviteService {

    @Autowired
    private UmsUserInviteMapper umsUserInviteMapper;
    @Autowired
    private UmsUserMapper umsUserMapper;
    @Autowired
    private CosService cosService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisConfig redisConfig;

    @Value("${prop.inviteUrl}")
    private String propInviteUrl;

    @Override
    public CommonResult<Boolean> addUserInvite(UmsUserInvite umsUserInvite) {
        Long userId = umsUserInvite.getUserId();
        if (BlankUtil.isEmpty(userId)) userId = UserUtil.getCurrentUser().getUserId();
        int user = umsUserInviteMapper.selectCount(new QueryWrapper<UmsUserInvite>().eq("user_id", userId));
        if (user > 0) return CommonResult.failed("你已被邀请", false);
        umsUserInvite.setUserInviteId(umsUserInvite.getUserInviteId()
                                                   .equals(NumberUtils.LONG_ZERO) ? IdWorker.generateId() : umsUserInvite.getUserInviteId());
        umsUserInvite.setUserId(userId);
        boolean b = save(umsUserInvite);
        if (b) {
            // 新增邀请后，更新团长的统计信息
            String fullKey = redisConfig.fullKey(redisConfig.getKey()
                                                            .getInviteUser()) + ":" + umsUserInvite.getParentId();
            redisService.del(fullKey);
            return CommonResult.success("success", true);
        } else return CommonResult.failed("error", false);
    }

    @Override
    public CommonResult<Boolean> updateUserRole(Long parentId) {
        UmsUser user = UserUtil.getCurrentUser();
        int num;
        if (parentId.equals(user.getUserId())) {
            return CommonResult.failed("扫描失败", false);
        }
        if (user.getRoleId() == 1) {
            return CommonResult.failed("您已是高级用户");
        } else {
            UmsUserInvite umsUserInvite = new UmsUserInvite();
            umsUserInvite.setParentId(parentId);
            umsUserInvite.setUserInviteId(NumberUtils.LONG_ZERO);
            if (addUserInvite(umsUserInvite).getCode() == 200) {
                user.setRoleId(1);
                num = umsUserMapper.updateById(user);
            } else {
                return addUserInvite(umsUserInvite);
            }
        }
        if (num > 0) {
            String fullKey = redisConfig.fullKey(redisConfig.getKey().getUser()) + ":" + user.getPhone();
            redisService.del(fullKey);
            return CommonResult.success("success", true);
        } else {
            return CommonResult.failed("error", false);
        }
    }

    @Override
    public CommonResult<String> getUserInviteQrCode(String type) throws Exception {
        // 根据用户获取COS二维码库中是否存在{该用户的二维码}，如果有则直接返回
        UmsUser currentUser = UserUtil.getCurrentUser();
        Long userId = currentUser.getUserId();
        // 校验高级用户
        if (NumberUtils.INTEGER_ZERO.equals(currentUser.getRoleId()))
            return CommonResult.failed(CommonResultCode.NOT_SENIOR_USER);
        // 用户二维码key，对应COS存储路径
        String key = String.format("%s%d-%s.jpg", ImgPathEnum.QR_CODE_PATH.value(), userId, type);
        Boolean existKey = cosService.isExistKey(key);
        // 存在直接返回
        if (existKey) return CommonResult.success(key);
        // 否则重新生成一张并上传到cos中，再返回,先创建临时文件
        File tempFile = File.createTempFile("temp-qrcode", null);
        // 构建二维码填充内容：邀请链接（当更换路径时需要清空cos/qrcode中的历史二维码图片）
        // -- 测试环境:http://reg.huwing.cn
        // -- 生成环境:http://regproduct.huwing.cn
        String content = String.format(propInviteUrl + "?uid=%d&type=%s", userId, type);
        // 开始生成
        Boolean encode = QRCodeUtil.encode(content, tempFile);
        if (encode) {
            // 开始上传
            URL upload = cosService.upload(key, tempFile);
            if (BlankUtil.isNotEmpty(upload)) return CommonResult.success(key);
        }
        return CommonResult.failed("生成二维码失败");
    }
}
