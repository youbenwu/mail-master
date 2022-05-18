package com.ys.mail.util;

import com.ys.mail.enums.AnsiColorEnum;
import org.slf4j.Logger;

/**
 * 日志工具类
 *
 * @author CRH
 * @date 2022-05-17 17:40
 * @since 1.0
 */
public class LoggerUtil {

    /**
     * 打印带颜色的信息（蓝色）
     *
     * @param logger  日志对象
     * @param content 内容
     */
    public static void info(Logger logger, String content) {
        String colorContent = StringUtil.getColorContent(AnsiColorEnum.BLUE, content);
        logger.warn(colorContent);
    }

    /**
     * 打印带颜色的信息（黄色）
     *
     * @param logger  日志对象
     * @param content 内容
     */
    public static void warn(Logger logger, String content) {
        String colorContent = StringUtil.getColorContent(AnsiColorEnum.YELLOW, content);
        logger.warn(colorContent);
    }

}
