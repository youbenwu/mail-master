package com.ys.mail.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Ansi 颜色码
 *
 * @author CRH
 * @date 2022-05-12 18:02
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum AnsiColorEnum {
    /**
     * Reset
     */
    RESET("\033[0m"),

    /**
     * Regular Colors
     */
    WHITE("\033[0;30m"),
    RED("\033[0;31m"),
    GREEN("\033[0;32m"),
    YELLOW("\033[0;33m"),
    BLUE("\033[0;34m"),
    PURPLE("\033[0;35m"),
    CYAN("\033[0;36m"),
    GREY("\033[0;37m"),

    /**
     * Bold
     */
    WHITE_BOLD("\033[1;30m"),
    RED_BOLD("\033[1;31m"),
    GREEN_BOLD("\033[1;32m"),
    YELLOW_BOLD("\033[1;33m"),
    BLUE_BOLD("\033[1;34m"),
    PURPLE_BOLD("\033[1;35m"),
    CYAN_BOLD("\033[1;36m"),
    GREY_BOLD("\033[1;37m"),

    /**
     * Underline
     */
    WHITE_UNDERLINED("\033[4;30m"),
    RED_UNDERLINED("\033[4;31m"),
    GREEN_UNDERLINED("\033[4;32m"),
    YELLOW_UNDERLINED("\033[4;33m"),
    BLUE_UNDERLINED("\033[4;34m"),
    PURPLE_UNDERLINED("\033[4;35m"),
    CYAN_UNDERLINED("\033[4;36m"),
    GREY_UNDERLINED("\033[4;37m"),

    /**
     * Background
     */
    WHITE_BACKGROUND("\033[40m"),
    RED_BACKGROUND("\033[41m"),
    GREEN_BACKGROUND("\033[42m"),
    YELLOW_BACKGROUND("\033[43m"),
    BLUE_BACKGROUND("\033[44m"),
    PURPLE_BACKGROUND("\033[45m"),
    CYAN_BACKGROUND("\033[46m"),
    GREY_BACKGROUND("\033[47m"),

    /**
     * High Intensity
     */
    WHITE_BRIGHT("\033[0;90m"),
    RED_BRIGHT("\033[0;91m"),
    GREEN_BRIGHT("\033[0;92m"),
    YELLOW_BRIGHT("\033[0;93m"),
    BLUE_BRIGHT("\033[0;94m"),
    PURPLE_BRIGHT("\033[0;95m"),
    CYAN_BRIGHT("\033[0;96m"),
    GREY_BRIGHT("\033[0;97m"),

    /**
     * Bold High Intensity
     */
    WHITE_BOLD_BRIGHT("\033[1;90m"),
    RED_BOLD_BRIGHT("\033[1;91m"),
    GREEN_BOLD_BRIGHT("\033[1;92m"),
    YELLOW_BOLD_BRIGHT("\033[1;93m"),
    BLUE_BOLD_BRIGHT("\033[1;94m"),
    PURPLE_BOLD_BRIGHT("\033[1;95m"),
    CYAN_BOLD_BRIGHT("\033[1;96m"),
    GREY_BOLD_BRIGHT("\033[1;97m"),

    /**
     * High Intensity backgrounds
     */
    WHITE_BACKGROUND_BRIGHT("\033[0;100m"),
    RED_BACKGROUND_BRIGHT("\033[0;101m"),
    GREEN_BACKGROUND_BRIGHT("\033[0;102m"),
    YELLOW_BACKGROUND_BRIGHT("\033[0;103m"),
    BLUE_BACKGROUND_BRIGHT("\033[0;104m"),
    PURPLE_BACKGROUND_BRIGHT("\033[0;105m"),
    CYAN_BACKGROUND_BRIGHT("\033[0;106m"),
    GREY_BACKGROUND_BRIGHT("\033[0;107m"),

    ;
    final String code;
}
