package com.ys.mail.enums;

import lombok.AllArgsConstructor;

/**
 * 存储主目录
 *
 * @author CRH
 * @date 2022-04-27 15:19
 * @since 1.0
 */
@AllArgsConstructor
public enum CosFolderEnum implements IPairs<Integer, String, CosFolderEnum> {

    /**
     * 主要存储图片
     */
    IMAGES_FOLDER(1, "/images"),

    /**
     * 主要存储文件
     */
    FILE_FOLDER(2, "/files");

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
