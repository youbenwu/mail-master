package com.ys.mail.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 图片地址分类的枚举
 *
 * @author 007
 */
@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public enum ImgPathEnum implements IPairs<Integer, String, ImgPathEnum> {

    /**
     * 图片路径
     */
    DOWNLOAD_QRCODE_PATH(-3, "下载二维码", "/download-qrcode/", null),
    SYS_LOGO_PATH(-2, "系统Logo", "/sysLogo/", null),
    TEST_PATH(-1, "开发测试", "/test/", 2 * 1024 * 1024L),
    SETTING_PATH(0, "系统设置", "/setting/", null),
    RELATIVE_PATH(1, "商品图片", "/product/", 1024 * 1024L),
    HEAD_PORTRAIT_PATH(2, "用户头像", "/headPortrait/", null),
    QR_CODE_PATH(3, "邀请二维码", "/qrcode/", null),
    APPRAISE_PATH(4, "商品评价", "/appraise/", null),
    ID_CARD_PATH(5, "用户身份证", "/idCard/", null),
    ADVERTISE_PATH(6, "轮播图", "/advertise/", 3 * 1024 * 1024L),
    IM_PATH(7, "融云聊天", "/im/", null),
    BRAND_PATH(8, "企业品牌", "/brand/", null),
    STORE_PATH(9, "用户店铺头像", "/store/", null),
    CASE_PICS_PATH(10, "创客商品图片案例", "/casePics/", 2 * 1024 * 1024L),
    ;

    final Integer key;
    final String value;
    /**
     * 相对路径
     */
    final String path;
    /**
     * 限制的图片最大大小，单位字节
     */
    final Long size;
}
