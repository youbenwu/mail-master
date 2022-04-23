package com.ys.mail.service.impl;

import com.ys.mail.controller.FileController;
import com.ys.mail.entity.OmsIntegralOrder;
import com.ys.mail.entity.UmsIntegral;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.exception.ApiException;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.IntegralConvertParam;
import com.ys.mail.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdGenerator;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.UserUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ys.mail.mapper.PmsIntegralProductMapper;
import com.ys.mail.entity.PmsIntegralProduct;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 积分商品信息表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-17
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class PmsIntegralProductServiceImpl extends ServiceImpl<PmsIntegralProductMapper, PmsIntegralProduct> implements PmsIntegralProductService {

    private final static Logger LOGGER = LoggerFactory.getLogger(PmsIntegralProductServiceImpl.class);

    @Autowired
    private PmsIntegralProductMapper integralProductMapper;
    @Autowired
    private OmsIntegralOrderService integralOrderService;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private UmsUserService userService;
    @Autowired
    private UmsIntegralService integralService;

    @Override
    public List<PmsIntegralProduct> getAllIntegralPdt(Long integralPdtId) {
        return integralProductMapper.selectAllIntegralPdt(integralPdtId);
    }

    @Override
    public CommonResult<Boolean> integralConvertPdt(IntegralConvertParam param) {
        //首先查询出积分够不够
        UmsUser currentUser = UserUtil.getCurrentUser();
        Long totalAmount = param.getTotalAmount();
        Long integralSum = currentUser.getIntegralSum();
        if(integralSum.compareTo(totalAmount) <= NumberUtils.INTEGER_ZERO){
            return CommonResult.failed("用户积分不够");
        }

        // 对比前端算出来的结果积分对不对
        Long integralPdtId = Long.valueOf(param.getIntegralPdtId());
        PmsIntegralProduct integralProduct = integralProductMapper.selectById(integralPdtId);
        if(BlankUtil.isEmpty(integralProduct)){
            return CommonResult.failed("商品信息有误");
        }
        long price = integralProduct.getPrice() * param.getQuantity();
        if(totalAmount.compareTo(price) != NumberUtils.INTEGER_ZERO){
            return CommonResult.failed("积分计算有误");
        }

        // 生成订单
        Date date = new Date();
        Long userId = currentUser.getUserId();
        OmsIntegralOrder integralOrder = new OmsIntegralOrder();
        BeanUtils.copyProperties(param,integralOrder);
        integralOrder.setIntegralPdtId(integralPdtId);
        integralOrder.setUserId(userId);
        integralOrder.setIntegralOrderId(IdWorker.generateId());
        integralOrder.setOrderSn(IdGenerator.INSTANCE.generateId());
        integralOrder.setOrderStatus(NumberUtils.INTEGER_ONE);
        integralOrder.setPaymentTime(date);
        boolean b = integralOrderService.save(integralOrder);
        if(!b){
            return CommonResult.failed("生成订单失败");
        }
        LOGGER.info("生成订单{}",true);

        //商品销量加
        boolean b1 = integralProductMapper.updateSaleById(integralPdtId,integralOrder.getQuantity());
        if(! b1){
            throw new ApiException("销量新增失败");
        }
        LOGGER.info("销量新增{}",true);

        // 减去用户的积分,修改用户表
        userCacheService.delUser(currentUser.getPhone());
        long remainNum = integralSum - totalAmount;
        currentUser.setIntegralSum(remainNum);
        boolean b2 = userService.updateById(currentUser);
        if(! b2){
            throw new ApiException("用户信息修改失败");
        }
        LOGGER.info("用户信息修改{}",true);

        //生成积分明细表,新增
        UmsIntegral build = UmsIntegral.builder()
                .integralId(IdWorker.generateId())
                .userId(userId)
                .changeType(NumberUtils.INTEGER_ONE)
                .changeCount(totalAmount)
                .sourceType(NumberUtils.INTEGER_ZERO)
                .build();
        boolean save = integralService.save(build);
        if(! save){
            throw new ApiException("积分明细失败");
        }
        LOGGER.info("积分明细{}",true);
        return CommonResult.success("success",true);
    }
}
