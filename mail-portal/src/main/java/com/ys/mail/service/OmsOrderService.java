package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.OmsOrder;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.Query;
import com.ys.mail.model.bo.GenerateOrderBO;
import com.ys.mail.model.dto.OrderInfoDTO;
import com.ys.mail.model.dto.QuickOrderDTO;
import com.ys.mail.model.param.GiftOrderParam;
import com.ys.mail.model.query.QuickOrderQuery;
import com.ys.mail.model.vo.ElectronicVo;
import com.ys.mail.model.vo.OmsOrderVO;
import com.ys.mail.model.vo.PartnerTodayResultsVO;
import com.ys.mail.model.vo.UserOrderVO;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-23
 */
public interface OmsOrderService extends IService<OmsOrder> {

    //查询订单状态
    List<OmsOrderVO> getOrderList(int orderStatus, Long orderId, Boolean cpyType, String productName);

    /**
     * 根据目标用户id查询相关的订单记录
     *
     * @param userId   目标用户id
     * @param orderId  翻页的订单id
     * @param pageSize 查询条数
     * @return 返回值
     */
    List<UserOrderVO> getUserOrderList(String userId, String orderId, Integer pageSize);

    /**
     * 支付回调成功后修改订单状态
     *
     * @param build 订单对象
     * @return 返回值
     */
    boolean updateOrder(OmsOrder build);

    /**
     * 修改公司类型
     *
     * @param orderSn 订单编号
     * @param cpyType 公司类型
     * @return 返回值
     */
    boolean updateByCpyType(String orderSn, Byte cpyType);

    /**
     * 订单详情
     *
     * @param orderSn 订单编号
     * @return 返回值
     */
    OrderInfoDTO getOrderInfo(String orderSn);

    /**
     * 生成礼品订单
     *
     * @return 订单支付对象
     */
    CommonResult<GenerateOrderBO> generateGiftOrder(String userImageString, String cpyType);

    /**
     * 更新礼品订单
     *
     * @return bool
     */
    CommonResult<Boolean> updateGiftOrder(GiftOrderParam param);

    /**
     * 查看当前用户的礼品订单
     *
     * @param userId 用户ID
     * @return 订单
     */
    OmsOrder getGiftOrder(Long userId);

    /**
     * 高级会员支付回调更新使用
     *
     * @param userId      用户ID
     * @param order       回调订单
     * @param totalAmount 交易金额
     * @return 是否成功
     */
    boolean updateUserForSeniorPay(Long userId, OmsOrder order, Long totalAmount);

    /**
     * 是待发货
     *
     * @param orderId 订单ID
     */
    void updateByOrderType(Long orderId);

    /**
     * 查询秒杀订单
     *
     * @param query 查询参数
     * @return 返回值
     */
    List<QuickOrderDTO> getAllQuickOrder(QuickOrderQuery query);

    PartnerTodayResultsVO partnerTodayResults(Long partnerId);

    Page<ElectronicVo> selectElectronic(Query query, Long partnerId);

    List<QuickOrderDTO> getAllMakerOrder(QuickOrderQuery query);

    /**
     * 订单详情
     *
     * @param orderSn 订单sn
     * @return 返回值
     */
    OrderInfoDTO getNewOrderInfo(String orderSn);
}
