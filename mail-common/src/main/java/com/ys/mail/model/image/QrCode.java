package com.ys.mail.model.image;

import lombok.Builder;
import lombok.Data;

/**
 * 二维码对象
 *
 * @author CRH
 * @date 2022-05-13 17:13
 * @since 1.0
 */
@Data
@Builder
public class QrCode {

    /**
     * 二维码内容
     */
    private String content;
    /**
     * 是否需要压缩
     */
    private boolean needCompress;
    /**
     * 二维码尺寸
     */
    private int qrcodeSize;
    /**
     * 二维码路径
     */
    private String qrPath;
    /**
     * 内嵌Logo路径
     */
    private String logoPath;
    /**
     * LOGO宽度
     */
    private int logoWidth;
    /**
     * LOGO高度
     */
    private int logoHeight;

}
