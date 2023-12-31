package com.ys.mail.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.config.GlobalConfig;
import com.ys.mail.constant.NumberConstant;
import com.ys.mail.entity.OmsOrder;
import com.ys.mail.exception.BusinessException;
import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.mapper.OmsOrderItemMapper;
import com.ys.mail.mapper.OmsOrderMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.OmsOrderQuery;
import com.ys.mail.model.admin.vo.*;
import com.ys.mail.model.vo.OmsOrderItemVO;
import com.ys.mail.override.ChainLinkedHashMap;
import com.ys.mail.service.KdService;
import com.ys.mail.service.OmsOrderService;
import com.ys.mail.util.*;
import com.ys.mail.wrapper.SqlQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-12-27
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class OmsOrderServiceImpl extends ServiceImpl<OmsOrderMapper, OmsOrder> implements OmsOrderService {

    @Autowired
    private OmsOrderMapper omsOrderMapper;
    @Autowired
    private OmsOrderItemMapper omsOrderItemMapper;
    @Autowired
    private GlobalConfig globalConfig;
    @Autowired
    private KdService kdService;

    @Override
    public CommonResult<Page<PcUserOrderVO>> getPage(OmsOrderQuery query) {
        Page<PcUserOrderVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        // 构建多条件查询
        SqlQueryWrapper<PcUserOrderVO> wrapper = this.getWrapper(query);
        return CommonResult.success(omsOrderMapper.getPage(page, wrapper));
    }

    private SqlQueryWrapper<PcUserOrderVO> getWrapper(OmsOrderQuery query) {
        // 构建多条件查询
        SqlQueryWrapper<PcUserOrderVO> wrapper = new SqlQueryWrapper<>();
        wrapper.likeRight("oo.user_id", query.getUserId())
               .like("oo.order_sn", query.getOrderSn())
               .like("oo.delivery_company", query.getDeliveryCompany())
               .like("oo.delivery_sn", query.getDeliverySn())
               .like("oo.trans_id", query.getTransId())
               .eq("oo.source_type", query.getSourceType())
               .eq("oo.pay_type", query.getPayType())
               .eq("oo.order_status", query.getOrderStatus())
               .eq("oo.order_type", query.getOrderType())
               .like("uu.nickname", query.getNickname())
               .like("uu.phone", query.getPhone())
               .compareDate("oo.create_time", query.getBeginTime(), query.getEndTime())
               .orderByDesc("oo.order_id");
        return wrapper;
    }

    @Override
    public List<PrPdtOrderVO> getByPrPdtOrder() {
        return omsOrderMapper.selectByPrPdtOrder();
    }

    @Override
    public List<PidPrPdtOrderVO> getByPidPrPdtOrder() {
        return omsOrderMapper.selectByPidPrPdtOrder();
    }

    @Override
    public List<OrdinaryReMoneyVO> getByOrdinaryReMoney(Integer ite) {
        return omsOrderMapper.selectByOrdinaryReMoney(ite);
    }

    @Override
    public List<OmsOrderItemVO> getItemList(Long orderId) {
        return omsOrderItemMapper.getItemList(orderId);
    }

    @Override
    public void exportExcel(OmsOrderQuery query, String fileName, HttpServletResponse response) {
        // 设置最大分页
        query.setPageSize(NumberConstant.TWO_THOUSAND);
        // 构建条件
        SqlQueryWrapper<PcUserOrderVO> wrapper = this.getWrapper(query);
        // 查询订单和订单详情
        List<OrderDetailsVO> list = omsOrderMapper.getOrderDetails(wrapper);
        // 工作表集合
        Map<String, List<Map<String, Object>>> workbookMap = new HashMap<>(1);
        // 封装数据
        List<Map<String, Object>> rows = new ArrayList<>();
        list.forEach(e -> {
            ChainLinkedHashMap<String, Object> map = new ChainLinkedHashMap<>();
            List<OmsOrderItemVO> omsOrderItem = e.getOmsOrderItem();
            if (BlankUtil.isNotEmpty(omsOrderItem)) {
                omsOrderItem.forEach(item -> {
                    map.putObj("用户ID", e.getUserId().toString())
                       .putObj("订单编号", item.getOrderSn())
                       .putObj("订单号", e.getOrderId().toString())
                       .putObj("昵称", e.getNickname())
                       .putObj("手机号", e.getPhone())
                       .putObj("客户端类型", globalConfig.appName(Integer.valueOf(e.getCpyType())))
                       .putObj("下单时间", e.getCreateTime())
                       .putObj("订单类型", EnumTool.getValue(OmsOrder.OrderType.class, e.getOrderType()))
                       .putObj("商品名称", item.getProductName())
                       .putObj("商品分类名称", item.getPdtCgyName())
                       .putObj("商品属性分类名称", item.getPdtAttributeCgyName())
                       .putObj("商品品牌", item.getProductBrand())
                       .putObj("商品货号", item.getProductSn())
                       .putObj("商品SKU属性", StringUtil.parseJsonArray(item.getSpData()))
                       .putObj("商品价格", DecimalUtil.longToStrForDivider(item.getProductPrice()))
                       .putObj("购买数量", item.getProductQuantity())
                       .putObj("支付金额", DecimalUtil.longToStrForDivider(e.getPayAmount()))
                       .putObj("支付方式", EnumTool.getValue(OmsOrder.PayType.class, e.getPayType()))
                       .putObj("支付时间", e.getPaymentTime())
                       .putObj("订单状态", EnumTool.getValue(OmsOrder.OrderStatus.class, e.getOrderStatus()))
                       .putObj("收货人姓名", e.getReceiverName())
                       .putObj("收货人电话", e.getReceiverPhone())
                       .putObj("收货人省份", e.getReceiverProvince())
                       .putObj("收货人城市", e.getReceiverCity())
                       .putObj("收货人地区", e.getReceiverRegion())
                       .putObj("收货人详细地址", e.getReceiverAddress())
                       .putObj("订单备注", e.getOrderNote())
                       .putObj("确认收货", NumberUtils.INTEGER_ZERO.equals(e.getIsConfirmStatus()) ? "未确认" : "已确认")
                       .putObj("交易流水号", e.getTransId())
                       .putObj("商品详情ID", BlankUtil.isNotEmpty(item.getOrderItemId()) ? item.getOrderItemId()
                                                                                           .toString() : null)
                       .putObj("商品ID", BlankUtil.isNotEmpty(item.getProductId()) ? item.getProductId()
                                                                                       .toString() : null);
                    rows.add(map);
                });
            } else {
                rows.add(map);
            }
        });
        workbookMap.put(fileName, rows);
        // 导出Excel（相同用户信息列暂不能合并）
        ExcelTool.writeExcel(workbookMap, globalConfig.getProjectName() + "-" + fileName, response);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean logistics(Long orderId, String deliverySn) {
        // TODO 可以添加也修改,有可能是添加错误的信息,就给修改的机会,暂时就一家物流,后面再重构吧!
        // 查询出快递单号是哪家,能后添加进去,快递鸟那边的 Shippers 为空
        JSONArray shippers = kdService.getLogistics(deliverySn);
        OmsOrder build = OmsOrder.builder()
                                 .orderId(orderId)
                                 .deliveryCompany(((JSONObject) shippers.get(NumberUtils.INTEGER_ZERO))
                                         .get("ShipperName").toString())
                                 .deliverySn(deliverySn)
                                 .orderStatus(OmsOrder.OrderStatus.TWO.key())
                                 .deliveryTime(new Date())
                                 .build();
        boolean response;
        try {
            response = omsOrderMapper.update(build);
        } catch (Exception e) {
            log.debug("订单id:{}物流单号添加失败", orderId);
            throw new BusinessException(BusinessErrorCode.ERR_UNIQUE_LOGISTICS);
        }
        return response;
    }

    @Override
    public String logistics(String deliverySn) {
        JSONArray shippers = kdService.getLogistics(deliverySn);
        return ((JSONObject) shippers.get(NumberUtils.INTEGER_ZERO)).get("ShipperName").toString();
    }

    @Override
    public JSONObject logisticsTrack(String deliverySn, String customerName) {
        // 此处可以单独抽出来..后台和app端都可以用
        JSONArray shippers = kdService.getLogistics(deliverySn);
        return kdService.logisticsTrack(deliverySn, ((JSONObject) shippers.get(NumberUtils.INTEGER_ZERO))
                .get("ShipperCode").toString(), customerName);
    }

}
