package com.ys.mail.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.PcReview;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.PcReviewParam;
import com.ys.mail.model.admin.query.PcReviewQuery;
import com.ys.mail.model.admin.vo.PcReviewVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 070
 * @since 2021-12-29
 */
public interface PcReviewService extends IService<PcReview> {

    /**
     * 后台分页查询
     *
     * @param query 查询条件
     * @return 结果
     */
    CommonResult<IPage<PcReviewVO>> getPage(PcReviewQuery query);

    /**
     * 审核提现
     *
     * @param param 参数
     * @return 结果
     */
    CommonResult<Boolean> updateReview(PcReviewParam param);

    /**
     * 获取指定日期的审核列表
     *
     * @param date 日期，如 2022-03-23
     * @return 结果
     */
    List<PcReview> getTodayList(String date);

    /**
     * 导出审核数据到 - Excel
     *
     * @param query    查询条件
     * @param fileName 文件名，不包含扩展名，如 xxx.xlsx 中的 xxx
     * @param response 响应
     */
    void exportExcel(PcReviewQuery query, String fileName, HttpServletResponse response);

}
