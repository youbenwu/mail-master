package com.ys.mail.util;

import com.ys.mail.constant.StringConstant;

/**
 * 系统工具
 *
 * @author CRH
 * @date 2022-05-23 10:17
 * @since 1.0
 */
public class SystemUtil {

    /**
     * 获取不同平台下的文件分隔符
     *
     * @return 分隔符
     */
    public static String getFileSeparator() {
        return System.getProperty("file.separator");
    }

    /**
     * 判断是否是Windows操作系统
     *
     * @return true->表示Windows，false表示其他平台
     */
    public static boolean isWindowsOs() {
        String osName = System.getProperty("os.name");
        return osName.toLowerCase().startsWith(StringConstant.WINDOWS);
    }

    /**
     * 获取操作系统中的临时目录
     *
     * @return 临时目录
     */
    public static String getTmpDir() {
        String tmpDir = System.getProperty("java.io.tmpdir");
        if (!SystemUtil.isWindowsOs()) {
            String fileSeparator = SystemUtil.getFileSeparator();
            if (!tmpDir.endsWith(fileSeparator)) {
                tmpDir += fileSeparator;
            }
        }
        return tmpDir;
    }

}
