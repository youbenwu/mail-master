package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.*;
import com.ys.mail.mapper.*;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.PageCommonResult;
import com.ys.mail.model.admin.query.DetailQuery;
import com.ys.mail.model.admin.query.Query;
import com.ys.mail.model.dto.OrderDetailDto;
import com.ys.mail.model.dto.OrderInfoDTO;
import com.ys.mail.model.dto.PartnerAddressDTO;
import com.ys.mail.model.dto.VerifyDto;
import com.ys.mail.model.vo.ElectronicVo;
import com.ys.mail.model.vo.MerchandiseVo;
import com.ys.mail.model.vo.OrderItemDetailsVO;
import com.ys.mail.model.vo.PartnerTodayResultsVO;
import com.ys.mail.service.OmsOrderService;
import com.ys.mail.service.UmsPartnerService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 070
 * @since 2022-01-19
 */
@Service
public class UmsPartnerServiceImpl extends ServiceImpl<UmsPartnerMapper, UmsPartner> implements UmsPartnerService {

    @Autowired
    private UmsPartnerMapper partnerMapper;
    @Autowired
    private OmsOrderService orderService;
    @Autowired
    private OmsOrderMapper omsOrderMapper;
    @Autowired
    private PmsProductMapper productMapper;
    @Autowired
    private UmsIncomeMapper incomeMapper;
    @Autowired
    private PmsVerificationCodeMapper verificationCodeMapper;
    @Autowired
    private PmsVerificationRecordsMapper verificationRecordsMapper;
    @Autowired
    private OmsVerificationOrderMapper verificationOrderMapper;
    @Autowired
    private OmsOrderItemMapper omsOrderItemMapper;
    @Autowired
    private UmsPartnerMapper umsPartnerMapper;
    @Autowired
    private UmsIncomeMapper umsIncomeMapper;


    @Override
    public CommonResult<PartnerTodayResultsVO> todayResults() {
        // 1. 首先要是合伙人
        Long userId = UserUtil.getCurrentUser().getUserId();
        UmsPartner umsPartner = partnerMapper.selectOne(Wrappers.<UmsPartner>lambdaQuery()
                                                                .eq(UmsPartner::getUserId, userId)
                                                                .eq(UmsPartner::getDeleted, 0));
        if (ObjectUtils.isEmpty(umsPartner)) {
            return CommonResult.failed("您还不是合伙人");
        }
        //得到合伙人id
        Long partnerId = umsPartner.getPartnerId();
        // 查询订单表得到今日业绩
//                PartnerTodayResultsVO vo1 = orderService.partnerTodayResults(partnerId);
        // 2022.2.18 修改为 核销 才计算 今日业绩
        PartnerTodayResultsVO vo = verificationRecordsMapper.partnerTodayResults(partnerId);
        if (BlankUtil.isNotEmpty(vo)) {
            /* 补充VO数据 start */
            // 订单数
            Integer orderNumber = vo.getOrderNumber();
            // 总金额
            Long totalAmount = vo.getTotalAmount();
            // 均价
            if (totalAmount != null) {
                BigDecimal divide = BigDecimal.valueOf(totalAmount)
                                              .divide(BigDecimal.valueOf(orderNumber), 0, RoundingMode.DOWN);
                vo.setAvgPrice(divide.longValue());
                // 访客数 todo:访客数暂时等于订单数
            } else {
                vo.setTotalAmount(0L);
                vo.setAvgPrice(0L);
            }
            vo.setVisitors(orderNumber);
            /* 补充VO数据 end */
        } else {
            vo = new PartnerTodayResultsVO(0);
        }
        return CommonResult.success(vo);
    }

    @Override
    public CommonResult<MerchandiseVo> merchandise(Query query) {
        Long userId = UserUtil.getCurrentUser().getUserId();
        UmsPartner umsPartner = partnerMapper.selectOne(Wrappers.<UmsPartner>lambdaQuery()
                                                                .eq(UmsPartner::getUserId, userId)
                                                                .eq(UmsPartner::getDeleted, 0));
        if (ObjectUtils.isEmpty(umsPartner)) {
            return CommonResult.failed("您还不是合伙人");
        }
        // 得到合伙人id
        Long partnerId = umsPartner.getPartnerId();
        IPage<PmsProduct> iPage = new Page<>(query.getPageNum(), query.getPageSize());
        iPage = productMapper.selectPage(iPage, Wrappers.<PmsProduct>lambdaQuery()
                                                        .eq(PmsProduct::getPartnerId, partnerId));
        List<MerchandiseVo> voList = new ArrayList<>();
        iPage.getRecords().forEach(obj -> {
            MerchandiseVo vo = new MerchandiseVo();
            BeanUtils.copyProperties(obj, vo);
            voList.add(vo);
        });
        PageCommonResult result = new PageCommonResult(query.getPageNum(), query.getPageSize(), (int) iPage.getTotal());
        result.setMessage("操作成功");
        result.setData(voList);
        return result;
    }

    @Override
    public CommonResult<ElectronicVo> electronic(Query query) {
        Long userId = UserUtil.getCurrentUser().getUserId();
        UmsPartner umsPartner = partnerMapper.selectOne(Wrappers.<UmsPartner>lambdaQuery()
                                                                .eq(UmsPartner::getUserId, userId)
                                                                .eq(UmsPartner::getDeleted, 0));
        if (ObjectUtils.isEmpty(umsPartner)) {
            return CommonResult.failed("您还不是合伙人");
        }
        // 得到合伙人id
        Long partnerId = umsPartner.getPartnerId();
        Page<ElectronicVo> voList = orderService.selectElectronic(query, partnerId);


        PageCommonResult result = new PageCommonResult(query.getPageNum(), query.getPageSize(), (int) voList.getTotal());
        result.setMessage("操作成功");
        result.setData(voList.getRecords());

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Boolean> verification(Map<String, String> params) {
        String code = params.get("code");
        if (StringUtils.isBlank(code)) {
            return CommonResult.failed("没有核销码信息");
        }

        PmsVerificationCode verificationCode = verificationCodeMapper.selectOne(Wrappers
                .<PmsVerificationCode>lambdaQuery()
                .eq(PmsVerificationCode::getCode, code));
        if (ObjectUtils.isEmpty(verificationCode)) {
            return CommonResult.failed("没有核销码信息");
        }

        // 状态 0待使用 1已使用 2已过期',
        Integer isStatus = verificationCode.getIsStatus();
        if (isStatus.equals(PmsVerificationCode.CODE_STATUS_TWO)) {
            return CommonResult.failed("核销码已过期");
        } else if (isStatus.equals(PmsVerificationCode.CODE_STATUS_ONE)) {
            return CommonResult.failed("核销码已使用");
        } else if (isStatus.equals(PmsVerificationCode.CODE_STATUS_ZERO)) {
            PmsVerificationRecords records = new PmsVerificationRecords();
            UmsPartner umsPartner = partnerMapper.selectById(verificationCode.getPartnerId());
            if (ObjectUtils.isEmpty(umsPartner)) {
                return CommonResult.failed("没有合伙人信息");
            }
            Long userId = umsPartner.getUserId();
            // 插入核销记录
            records.setRecordId(IdWorker.generateId());
            records.setCode(code);
            // 合伙人id
            records.setPartnerId(verificationCode.getPartnerId());
            VerifyDto dto = verificationCodeMapper.queryDetail(code);
            records.setOrderId(dto.getOrderSn());
            records.setProductName(dto.getProductName());
            records.setProductPrice(dto.getProductPrice());
            records.setProductPic(dto.getProductPic());
            records.setTotalPrice(dto.getProductPrice());

            records.setPartnerName(umsPartner.getRealName());
            records.setCreateTime(new Date());
            records.setUpdateTime(new Date());
            verificationRecordsMapper.insert(records);

            // 更新码的状态
            verificationCode.setIsStatus(PmsVerificationCode.CODE_STATUS_ONE);
            verificationCodeMapper.updateById(verificationCode);

            // 更新订单状态
            OrderInfoDTO orderInfo = orderService.getOrderInfo(dto.getOrderSn());
            omsOrderMapper.updateByOrderTypeEight(orderInfo.getOrderId());

            // 拿原始价格  查产品表
            Long productPrice = orderInfo.getPartnerPrice();
            BigDecimal divide = BigDecimal.valueOf(productPrice);
            // {收益}---------并发
            UmsIncome newest = umsIncomeMapper.selectNewestByUserId(userId);
            if (newest != null) {
                Date createTime = newest.getCreateTime();
                Date now = new Date();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String createTimeS = simpleDateFormat.format(createTime);
                String nowS = simpleDateFormat.format(now);
                // 查询最新一条 str 不相等  = 0L
                if (!nowS.equals(createTimeS)) {
                    newest.setTodayIncome(0L);
                }
            }

            // 收益表.(最新)今日收益
            Long todayIncome = newest != null ? newest.getTodayIncome() : 0L;
            // 收益表.(最新)总收益
            Long allIncome = newest != null ? newest.getAllIncome() : 0L;
            // 收益表.(最新)结余
            Long balance = newest != null ? newest.getBalance() : 0L;

            // Bg.(最新)今日收益
            BigDecimal todayIncomeBg = new BigDecimal(todayIncome);
            // Bg.(最新)总收益
            BigDecimal allIncomeBg = new BigDecimal(allIncome);
            // Bg.(最新)结余
            BigDecimal balanceBg = new BigDecimal(balance);

            // 此次计算今日收益
            BigDecimal todayIncomeBgAddIncome = todayIncomeBg.add(divide);
            // 此次总收益
            BigDecimal allIncomeBgAddIncome = allIncomeBg.add(divide);
            // 此次结余
            BigDecimal balanceBgBgAddIncome = balanceBg.add(divide);
            // 收益表数据
            UmsIncome umsIncome = new UmsIncome();
            // 收益ID
            umsIncome.setIncomeId(IdWorker.generateId());
            // 收益人
            umsIncome.setUserId(userId);
            // 收益数
            umsIncome.setIncome(divide.longValue());
            // 今日收益
            umsIncome.setTodayIncome(todayIncomeBgAddIncome.longValue());
            // 总收益
            umsIncome.setAllIncome(allIncomeBgAddIncome.longValue());
            // 结余
            umsIncome.setBalance(balanceBgBgAddIncome.longValue());
            // 收益类型  1-秒杀收益  2022-02-11 修改为 6 -分佣(团长收益)
            umsIncome.setIncomeType(7);
            // 增加到余额收入
            umsIncome.setPayType(dto.getPayType());
            // 来源
            umsIncome.setDetailSource("核销收入");
            // 备注
            umsIncome.setRemark("核销收入-产生的金额总值:{" + productPrice + "}");
            umsIncomeMapper.insert(umsIncome);

        }
        return CommonResult.success(true);
    }

    @Override
    public CommonResult<PmsVerificationRecords> queryDetail(DetailQuery query) {
        Long userId = UserUtil.getCurrentUser().getUserId();
        UmsPartner umsPartner = partnerMapper.selectOne(Wrappers.<UmsPartner>lambdaQuery()
                                                                .eq(UmsPartner::getUserId, userId)
                                                                .eq(UmsPartner::getDeleted, 0));
        if (ObjectUtils.isEmpty(umsPartner)) {
            return PageCommonResult.failed("您还不是合伙人");
        }
        // 得到合伙人id
        Long partnerId = umsPartner.getPartnerId();

        int month = query.getMonth() - 1;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, month);
        c.set(Calendar.YEAR, query.getYear());
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String first = format.format(c.getTime());

        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.MONTH, month);
        ca.set(Calendar.YEAR, query.getYear());
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format.format(ca.getTime());

        IPage<PmsVerificationRecords> ipage = new Page<>(query.getPageNum(), query.getPageSize());
        ipage = verificationRecordsMapper.selectPage(ipage, Wrappers.<PmsVerificationRecords>lambdaQuery()
                                                                    .eq(PmsVerificationRecords::getPartnerId, partnerId)
                                                                    .between(PmsVerificationRecords::getCreateTime, first, last)
                                                                    .orderByDesc(PmsVerificationRecords::getCreateTime));
        PageCommonResult result = new PageCommonResult(query.getPageNum(), query.getPageSize(), (int) ipage.getTotal());
        result.setMessage("操作成功");
        result.setData(ipage.getRecords());
        return result;
    }

    @Override
    public CommonResult<OrderDetailDto> verifDetail(Long recordId) {
        OrderDetailDto dto = verificationRecordsMapper.selectOrderDetail(recordId);
        PmsVerificationCode verificationCode = verificationCodeMapper.selectOne(Wrappers
                .<PmsVerificationCode>lambdaQuery().eq(PmsVerificationCode::getCode, dto.getCode()));
        dto.setStatus(verificationCode.getIsStatus());
        return CommonResult.success(dto);
    }

    @Override
    public CommonResult<PmsVerificationRecords> verificationList(Query query) {
        Long userId = UserUtil.getCurrentUser().getUserId();
        UmsPartner umsPartner = partnerMapper.selectOne(Wrappers.<UmsPartner>lambdaQuery()
                                                                .eq(UmsPartner::getUserId, userId)
                                                                .eq(UmsPartner::getDeleted, 0));
        if (ObjectUtils.isEmpty(umsPartner)) {
            return PageCommonResult.failed("您还不是合伙人");
        }
        // 得到合伙人id
        Long partnerId = umsPartner.getPartnerId();
        IPage<PmsVerificationRecords> iPage = new Page<>(query.getPageNum(), query.getPageSize());
        iPage = verificationRecordsMapper.selectPage(iPage, Wrappers.<PmsVerificationRecords>lambdaQuery()
                                                                    .eq(PmsVerificationRecords::getPartnerId, partnerId));
        PageCommonResult result = new PageCommonResult(query.getPageNum(), query.getPageSize(), (int) iPage.getTotal());
        result.setMessage("操作成功");
        result.setData(iPage.getRecords());
        return result;
    }

    @Override
    public CommonResult<OrderInfoDTO> orderDetail(Long orderId) {
        OmsOrder order = orderService.getById(orderId);
        if (ObjectUtils.isEmpty(order)) {
            return CommonResult.failed("无此订单信息");
        }
        OrderInfoDTO dto = new OrderInfoDTO();

        // 商品核销码信息
        OmsVerificationOrder verificationOrder = verificationOrderMapper.selectOne(Wrappers
                .<OmsVerificationOrder>lambdaQuery()
                .eq(OmsVerificationOrder::getOrderId, order.getOrderSn()));
        if (ObjectUtils.isNotEmpty(verificationOrder)) {
            PmsVerificationCode verificationCode = verificationCodeMapper.selectById(verificationOrder.getVerificationId());
            dto.setCode(verificationCode.getCode());
            dto.setIsStatus(verificationCode.getIsStatus().toString());
        }

        // 商品合伙人信息
        UmsPartner umsPartner = umsPartnerMapper.selectById(order.getPartnerId());
        if (ObjectUtils.isNotEmpty(umsPartner)) {
            BeanUtils.copyProperties(umsPartner, dto);
        }

        BeanUtils.copyProperties(order, dto);
        // dto.setKdName(order);
        List<OmsOrderItem> omsOrderItemList = omsOrderItemMapper.selectList(Wrappers.<OmsOrderItem>lambdaQuery()
                                                                                    .eq(OmsOrderItem::getOrderId, order.getOrderId()));
        List<OrderItemDetailsVO> voList = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(omsOrderItemList)) {
            for (OmsOrderItem orderItem : omsOrderItemList) {
                OrderItemDetailsVO vo = new OrderItemDetailsVO();
                BeanUtils.copyProperties(orderItem, vo);
                vo.setSpData(orderItem.getProductAttr());
                voList.add(vo);
            }
        }
        dto.setOmsOrderItem(voList);
        return CommonResult.success(dto);
    }

    @Override
    public PartnerAddressDTO getAddressByProductId(Long productId) {
        return umsPartnerMapper.getAddressByProductId(productId);
    }
}
