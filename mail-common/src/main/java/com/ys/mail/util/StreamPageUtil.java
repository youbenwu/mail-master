package com.ys.mail.util;

import cn.hutool.core.util.PageUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Desc 流分页工具
 * @Author CRH
 * @Create 2022-03-10 13:27
 */
public class StreamPageUtil {

    /**
     * 将list进行模拟分页，不涉及数据库，使用流实现
     *
     * @param list     所有数据集
     * @param pageNum  当前页码
     * @param pageSize 分页大小
     * @return 封装好的分页结构数据
     */
    public static <T> Map<String, Object> getPage(Collection<T> list, int pageNum, int pageSize) {
        Map<String, Object> resultMap = new HashMap<>(5);

        // 初始化数据
        int total = 0;
        int pages = 0;
        long current = pageNum > 0 ? pageNum : 1;
        long size = pageSize > 0 ? pageSize : 10;
        Collection<T> records = new ArrayList<>();

        // 分页过滤
        if (BlankUtil.isNotEmpty(list)) {
            total = list.size();
            pages = PageUtil.totalPage(total, pageSize);
            if (current > pages) current = pages;
            if (size > total) size = total;
            records = list.stream().skip(pageSize * (current - 1)).limit(size).collect(Collectors.toList());
        }

        // 封装结果
        resultMap.put("total", total);// 总记录数
        resultMap.put("pages", pages);// 总页数
        resultMap.put("current", current);// 当前页码
        resultMap.put("size", size);// 分页条数
        resultMap.put("records", records);// 记录数

        return resultMap;
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            list.add(i);
        }
        Map<String, Object> page = StreamPageUtil.getPage(list, 11, 3);
        System.out.println(page);
    }

}
