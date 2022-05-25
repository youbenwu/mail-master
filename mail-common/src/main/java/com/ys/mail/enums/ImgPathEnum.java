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
     * 下载二维码
     */
    DOWNLOAD_QRCODE_PATH(-3, "/download-qrcode/", null),
    /**
     * 系统Logo
     */
    SYS_LOGO_PATH(-2, "/sysLogo/", null),
    /**
     * 开发测试，默认类型
     */
    TEST_PATH(-1, "/test/", 2 * 1024 * 1024L),
    /**
     * 系统设置
     */
    SETTING_PATH(0, "/setting/", null),
    /**
     * 商品图片
     */
    RELATIVE_PATH(1, "/product/", 1024 * 1024L),
    /**
     * 用户头像
     */
    HEAD_PORTRAIT_PATH(2, "/headPortrait/", null),
    /**
     * 二维码
     */
    QR_CODE_PATH(3, "/qrcode/", null),
    /**
     * 商品评价
     */
    APPRAISE_PATH(4, "/appraise/", null),
    /**
     * 用户身份证
     */
    ID_CARD_PATH(5, "/idCard/", null),
    /**
     * 轮播图
     */
    ADVERTISE_PATH(6, "/advertise/", null),
    /**
     * 融云聊天
     */
    IM_PATH(7, "/im/", null),
    /**
     * 企业品牌
     */
    BRAND_PATH(8, "/brand/", null),
    /**
     * 用户店铺头像
     */
    STORE_PATH(9, "/store/", null),
    /**
     * 创客商品图片案例
     */
    CASE_PICS_PATH(10, "/casePics/", 2 * 1024 * 1024L),
    ;

    final Integer key;
    final String value;
    /**
     * 限制的图片最大大小，单位字节
     */
    final Long size;
}
