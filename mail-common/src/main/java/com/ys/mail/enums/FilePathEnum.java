package com.ys.mail.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 文件地址分类枚举
 *
 * @author CRH
 * @date 2022-04-19 15:19
 * @since 1.0
 */
@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public enum FilePathEnum implements IPairs<Integer, String, FilePathEnum> {
    /**
     * 文件目录
     */
    TEST_PATH(-1, "开发测试", "/test/", null),
    APK_PATH(0, "APK存储", "/apk/", Collections.singletonList("apk")),
    VIDEO_PATH(1, "视频存储", "/video/", Arrays.asList("avi", "wmv", "mpeg", "mp4", "mov", "flv", "m4v", "aac", "rmvb", "3gp", "ts")),
    AUDIO_PATH(1, "音频存储", "/audio/", Arrays.asList("mp3", "wav", "flac", "aac", "alac")),
    ;

    final Integer key;
    final String value;
    final String path;
    /**
     * 文件后缀，不同存储目录要求不同，为空则不进行检测
     */
    final List<String> suffix;
}
