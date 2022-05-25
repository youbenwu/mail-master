package com.ys.mail.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ys.mail.model.CommonResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 主要对一些结果封装统一数据
 *
 * @author CRH
 * @date 2022-05-16 10:26
 * @since 1.0
 */
public class ResultUtil {

    /**
     * 给Map设置统一参数
     *
     * @param map  原map
     * @param time 过期时间，单位s
     */
    public static void setUnifyParam(Map<String, Object> map, int time) {
        DateTime tillTime = DateUtil.date();
        DateTime offsetHour = DateUtil.offsetSecond(tillTime, time);
        // 截止统计时间
        map.put("tillTime", tillTime);
        // 预计刷新时间
        map.put("refreshTime", offsetHour);
    }

    /**
     * 拼接方法参数名称和参数值
     *
     * @param paramNames  参数名称列表
     * @param args        参数值列表
     * @param includeList 需要包含的参数，长度为0则全部，否则按需拼接
     * @return 拼接后的集合
     */
    public static List<String> joinParamNameAndValue(String[] paramNames, Object[] args, String[] includeList) {
        List<String> resultList = new ArrayList<>();
        if (BlankUtil.isNotEmpty(includeList) && includeList.length <= args.length) {
            for (String s : includeList) {
                int index = Integer.parseInt(s);
                if (index >= args.length) continue;
                Object arg = args[index];
                if (BlankUtil.isEmpty(arg)) {
                    break;
                }
                resultList.add(paramNames[index] + ":" + arg);
            }
        } else {
            for (int i = 0; i < args.length; i++) {
                if (BlankUtil.isEmpty(args[i])) {
                    break;
                }
                resultList.add(paramNames[i] + ":" + args[i]);
            }
        }
        return resultList;
    }

    /**
     * 处理结果
     *
     * @param result 结果条件
     * @return 对应的结果
     */
    public static CommonResult<Boolean> isOk(boolean result) {
        return result ? CommonResult.success(true) : CommonResult.failed(false);
    }

    public static CommonResult<Boolean> isOk(String message, boolean result) {
        return result ? CommonResult.success(message, true) : CommonResult.failed(message, false);
    }
}
