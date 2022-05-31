package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.OmsOrder;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.OmsOrderQuery;
import com.ys.mail.model.admin.vo.OrdinaryReMoneyVO;
import com.ys.mail.model.admin.vo.PcUserOrderVO;
import com.ys.mail.model.admin.vo.PidPrPdtOrderVO;
import com.ys.mail.model.admin.vo.PrPdtOrderVO;
import com.ys.mail.model.vo.OmsOrderItemVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-12-27
 */
public interface OmsOrderService extends IService<OmsOrder> {

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页的订单数据
     */
    CommonResult<Page<PcUserOrderVO>> getPage(OmsOrderQuery query);

    /**
     * 查询前一日总共
     *
     * @return 结果
     */
    List<PrPdtOrderVO> getByPrPdtOrder();

    /**
     * 查询当天下的创客订单返还给邀请人
     *
     * @return 返回值
     */
    List<PidPrPdtOrderVO> getByPidPrPdtOrder();

    /**
     * 普通订单和会员订单返佣共用
     *
     * @param ite 类型区分
     * @return 返回值
     */
    List<OrdinaryReMoneyVO> getByOrdinaryReMoney(Integer ite);

    /**
     * 获取订单详情列表
     *
     * @param orderId 订单ID
     * @return 列表
     */
    List<OmsOrderItemVO> getItemList(Long orderId);

    /**
     * 导出数据到 -> Excel
     *
     * @param query    查询条件
     * @param fileName 文件名，不包含扩展名，如 xxx.xlsx 中的 xxx
     * @param response 响应
     */
    void exportExcel(OmsOrderQuery query, String fileName, HttpServletResponse response);
}
