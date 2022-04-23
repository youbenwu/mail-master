package com.ys.mail.enums;

import lombok.AllArgsConstructor;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-03-25 17:03
 */
@AllArgsConstructor
public enum EnumCosFolder implements IPairs<Integer, String, EnumCosFolder> {

    IMAGES_FOLDER(1, "/mail.huwing.cn"),
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
