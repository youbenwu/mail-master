package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.OmsOrder;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.dto.ExportOrderDTO;
import com.ys.mail.model.admin.param.ExportOrderParam;
import com.ys.mail.model.admin.query.OmsOrderQuery;
import com.ys.mail.model.admin.vo.PcUserOrderVO;
import com.ys.mail.model.admin.vo.PrPdtOrderVO;

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
     * 根据参数对象查询出普通订单和礼品订单
     *
     * @param query 查询对象
     * @return 分页的订单数据
     */
    CommonResult<Page<PcUserOrderVO>> getGeneralOrder(OmsOrderQuery query);

    /**
     * 获取需要导出的订单列表，不带分页，暂时根据条件查询所有数据
     *
     * @param params 过滤条件
     * @return 列表
     */
    List<ExportOrderDTO> getExportOrderList(ExportOrderParam params);

    CommonResult<String> export(ExportOrderParam params);

    /**
     * 查询前一日总共
     *
     * @return
     */
    List<PrPdtOrderVO> getByPrPdtOrder();
}
