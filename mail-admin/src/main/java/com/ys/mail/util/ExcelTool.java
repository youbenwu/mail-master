package com.ys.mail.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Desc Excel工具
 * @Author CRH
 * @Create 2022-03-23 09:33
 */
public class ExcelTool {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelTool.class);

    /**
     * 将数据导出到流中
     *
     * @param workbookMap 多个工作表，数据结构：Map<工作表名称,工作表数据> --> 工作表数据<Map<列名,值>>，建议采用LinkedHashMap保证输出顺序
     * @param fileName    文件名称前缀
     * @param response    响应，前端使用xlsx保存
     */
    public static void writeExcel(Map<String, List<Map<String, Object>>> workbookMap, String fileName, HttpServletResponse response) {
        // 自动关闭
        try (ExcelWriter writer = ExcelUtil.getWriter(true);
             ServletOutputStream out = response.getOutputStream()) {
            // 统一写出
            writeExcel(writer, workbookMap);
            // 设置响应头信息
            String fullName = String.format("%s-%s.xlsx", fileName, DateTool.getCurrentDateTime());
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-Disposition", writer.getDisposition(fullName, CharsetUtil.CHARSET_UTF_8));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            // 写到流中并关闭
            writer.flush(out, true);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 将数据导出到Excel文件中
     *
     * @param workbookMap 多个工作表，数据结构同上
     * @param filePath    完整的文件路径，包含后缀，支持xls、xlsx
     */
    public static void writeExcelToFile(Map<String, List<Map<String, Object>>> workbookMap, String filePath) {
        // 存在将删除
        if (FileUtil.exist(filePath)) FileUtil.del(filePath);
        // 自动关闭
        try (ExcelWriter writer = ExcelUtil.getBigWriter(filePath)) {
            // 统一写出
            writeExcel(writer, workbookMap);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 统一写入
     *
     * @param writer      写入器
     * @param workbookMap 工作表
     */
    private static void writeExcel(ExcelWriter writer, Map<String, List<Map<String, Object>>> workbookMap) {
        try (CostTimeUtil ignored = new CostTimeUtil("WriteExcel")) {
            // 开启：仅设置别名的列有效
            writer.setOnlyAlias(true);
            // 自定义样式
            setCustomStyle(writer);
            // 工作表下标
            AtomicInteger index = new AtomicInteger();
            // 总记录数
            AtomicInteger total = new AtomicInteger();
            // 遍历写入
            workbookMap.forEach((sheetName, data) -> {
                // 切换sheet
                if (index.get() == 0) {
                    writer.renameSheet(0, sheetName);
                } else {
                    writer.setSheet(sheetName);
                }
                // 冻结首行
                writer.setFreezePane(1);
                // 写入
                writer.write(data, true);
                // 条数
                index.getAndIncrement();
                total.getAndAdd(data.size());
                LOGGER.info("【写入Excel成功】-[工作表：{}]- [记录数：{}条]", sheetName, data.size());
            });
            LOGGER.info("【写入Excel完成】-[总记录数：{}条]", total);
        } catch (Exception e) {
            LOGGER.error("【写入Excel失败】- 异常结果：{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 自定义样式
     *
     * @param writer 写入器
     */
    private static void setCustomStyle(ExcelWriter writer) {
        StyleSet styleSet = writer.getStyleSet();
        // 设置标题字体（大小、粗细、字体等）
        CellStyle headCellStyle = styleSet.getHeadCellStyle();
        Font font = writer.createFont();
        font.setFontName("Microsoft YaHei UI");
        headCellStyle.setFont(font);
        headCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        // 设置全局字体
        font = writer.createFont();
        font.setFontName("楷体");
        styleSet.setFont(font, true);
        // 设置全局边框
        styleSet.setBorder(BorderStyle.THIN, IndexedColors.BLACK);
        styleSet.setBackgroundColor(IndexedColors.WHITE1, false);
    }
}
