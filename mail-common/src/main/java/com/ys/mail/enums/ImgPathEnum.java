package com.ys.mail.enums;

import lombok.AllArgsConstructor;

/**
 * 图片地址分类的枚举
 *
 * @author 007
 */
@AllArgsConstructor
public enum ImgPathEnum implements IPairs<Integer, String, ImgPathEnum> {

    /**
     * 下载二维码
     */
    DOWNLOAD_QRCODE_PATH(-3, "/download-qrcode/"),
    /**
     * 系统Logo
     */
    SYS_LOGO_PATH(-2, "/sysLogo/"),
    /**
     * 开发测试，默认类型
     */
    TEST_PATH(-1, "/test/"),
    /**
     * 系统设置
     */
    SETTING_PATH(0, "/setting/"),
    /**
     * 商品图片
     */
    RELATIVE_PATH(1, "/product/"),
    /**
     * 用户头像
     */
    HEAD_PORTRAIT_PATH(2, "/headPortrait/"),
    /**
     * 二维码
     */
    QR_CODE_PATH(3, "/qrcode/"),
    /**
     * 商品评价
     */
    APPRAISE_PATH(4, "/appraise/"),
    /**
     * 用户身份证
     */
    ID_CARD_PATH(5, "/idCard/"),
    /**
     * 轮播图
     */
    ADVERTISE_PATH(6, "/advertise/"),
    /**
     * 融云聊天
     */
    IM_PATH(7, "/im/"),
    /**
     * 企业品牌
     */
    BRAND_PATH(8, "/brand/"),
    /**
     * 用户店铺头像
     */
    STORE_PATH(9, "/store/"),
    /**
     * 创客商品图片案例
     */
    CASE_PICS_PATH(10, "/casePics/"),
    ;

    final Integer type;
    final String path;

    @Override
    public Integer key() {
        return this.type;
    }

    @Override
    public String value() {
        return this.path;
    }
}
