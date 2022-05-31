package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.OmsOrder;
import com.ys.mail.model.admin.dto.ExportOrderDTO;
import com.ys.mail.model.admin.dto.excel.OrderCollectDTO;
import com.ys.mail.model.admin.dto.excel.UserOrderDetailsDTO;
import com.ys.mail.model.admin.param.ExportOrderParam;
import com.ys.mail.model.admin.vo.*;
import com.ys.mail.model.dto.OmsOrderDTO;
import com.ys.mail.model.dto.OrderInfoDTO;
import com.ys.mail.model.dto.QuickOrderDTO;
import com.ys.mail.model.query.QuickOrderQuery;
import com.ys.mail.model.vo.ElectronicVo;
import com.ys.mail.model.vo.OmsOrderVO;
import com.ys.mail.model.vo.PartnerTodayResultsVO;
import com.ys.mail.model.vo.UserOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-23
 */
@Mapper
public interface OmsOrderMapper extends BaseMapper<OmsOrder> {
    /**
     * 根据用户id，订单状态查询列表信息
     *
     * @param omsOrderDTO 参数
     * @return 返回
     */
    List<OmsOrderVO> selectOrderList(@Param("omsOrderDTO") OmsOrderDTO omsOrderDTO);

    /**
     * 查询下级用户订单
     *
     * @param userId    下级用于ID
     * @param parentId  父级ID
     * @param isCurrent 是否只查询当月，否则查询全部
     * @param orderId   最后一条订单ID，用于分页
     * @param pageSize  分页大小
     * @return 结果
     */
    List<UserOrderVO> getUserOrderList(@Param("userId") Long userId, @Param("parentId") Long parentId,
                                       @Param("isCurrent") boolean isCurrent, @Param("orderId") String orderId, @Param("pageSize") Integer pageSize);

    /**
     * 支付成功后修改订单
     *
     * @param build 订单对象
     * @return 返回值
     */
    boolean updateOrder(@Param("build") OmsOrder build);

    /**
     * 修改订单公司类型
     *
     * @param orderSn 订单编号
     * @param cpyType 公司类型
     * @return 返回值
     */
    boolean updateByCpyType(@Param("orderSn") String orderSn, @Param("cpyType") Byte cpyType);

    /**
     * 订单详情
     *
     * @param orderSn 订单编号
     * @return 返回值
     */
    OrderInfoDTO selectOrderInfo(@Param("orderSn") String orderSn);

    void updateByOrderType(@Param("orderId") Long orderId);

    List<OmsOrder> findByParentIds(@Param("parentIds") List<Long> parentIds);

    /**
     * 查询秒杀订单
     *
     * @param query  查询参数
     * @param userId 用户id
     * @return 返回值
     */
    List<QuickOrderDTO> selectAllQuickOrder(@Param("query") QuickOrderQuery query, @Param("userId") Long userId);

    Long queryMoney(@Param("userIds") List<Long> userids, @Param("yyyyMMdd") String yyyyMMdd);

    List<ExportOrderDTO> getExportOrderList(@Param("params") ExportOrderParam params);

    PartnerTodayResultsVO selectTodayScore(@Param("partnerId") Long partnerId);

    Page<ElectronicVo> selectElectronic(Page page, @Param("partnerId") Long partnerId);

    void updateByOrderTypeEight(@Param("orderId") Long orderId);

    Page<PcUserOrderVO> getPage(Page<PcUserOrderVO> page, @Param(Constants.WRAPPER) Wrapper<PcUserOrderVO> queryWrapper);

    /**
     * 查询前一天产生的合伙人订单
     *
     * @return 返回值
     */
    List<PrPdtOrderVO> selectByPrPdtOrder();

    /**
     * 查询创客订单
     *
     * @param query  查询对象
     * @param userId 用户id
     * @return 返回值
     */
    List<QuickOrderDTO> selectAllMakerOrder(@Param("query") QuickOrderQuery query, @Param("userId") Long userId);

    /**
     * 获取订单汇总数据
     *
     * @return 列表
     */
    List<OrderCollectDTO> getOrderCollect();

    /**
     * 获取用户订单详情数据
     *
     * @param userId 用户ID
     * @return 结果
     */
    List<UserOrderDetailsDTO> getUserOrderDetailsData(@Param("userId") Long userId);

    /**
     * 查询出创客待返还给推荐人
     *
     * @return 返回值
     */
    List<PidPrPdtOrderVO> selectByPidPrPdtOrder();

    /**
     * 查询出返佣对象
     *
     * @param ite 参数
     * @return 返回值
     */
    List<OrdinaryReMoneyVO> selectByOrdinaryReMoney(@Param("ite") Integer ite);

    /**
     * 获取订单与详情
     *
     * @param wrapper 条件
     * @return 列表
     */
    List<OrderDetailsVO> getOrderDetails(@Param(Constants.WRAPPER) Wrapper<PcUserOrderVO> wrapper);
}
