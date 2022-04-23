package com.ys.mail.enums;

import lombok.AllArgsConstructor;

/**
 * 文件地址分类枚举
 *
 * @author 007
 */
@AllArgsConstructor
public enum EnumFilePath implements IPairs<Integer, String, EnumFilePath> {
    /**
     * 开发测试，默认类型
     */
    TEST_PATH(-1, "/test/"),
    /**
     * APK存储路径
     */
    APK_PATH(0, "/apk/"),
    /**
     * 视频存储路径
     */
    VIDEO_PATH(1, "/video/"),
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
