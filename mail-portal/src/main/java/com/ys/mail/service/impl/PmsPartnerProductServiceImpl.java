package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.OmsOrder;
import com.ys.mail.entity.OmsOrderItem;
import com.ys.mail.entity.PmsPartnerProduct;
import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.exception.code.IErrorCode;
import com.ys.mail.mapper.PmsPartnerProductMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.bo.GenerateOrderBO;
import com.ys.mail.model.dto.PartnerProductDTO;
import com.ys.mail.model.param.PartnerGenerateOrderParam;
import com.ys.mail.model.po.PartnerProductPO;
import com.ys.mail.service.PmsPartnerProductService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ghdhj
 */
@Service
public class PmsPartnerProductServiceImpl extends ServiceImpl<PmsPartnerProductMapper, PmsPartnerProduct> implements PmsPartnerProductService {

    private final static Logger log = LoggerFactory.getLogger(PmsPartnerProductServiceImpl.class);

    @Autowired
    private PmsPartnerProductMapper partnerProductMapper;

    @Override
    public List<PartnerProductDTO> list(Byte more, String partnerPdtId) {
        return partnerProductMapper.selectList(more, Long.valueOf(partnerPdtId));
    }

    @Override
    public PartnerProductPO info(String partnerPdtId) {
        return partnerProductMapper.selectByPrPdtId(Long.valueOf(partnerPdtId));
    }

    @Override
    public PartnerProductDTO buy(String partnerPdtId) {
        return partnerProductMapper.selectBuy(Long.valueOf(partnerPdtId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized CommonResult<GenerateOrderBO> generateOrder(PartnerGenerateOrderParam param) {
        /**
         * 生成订单规则:首次第一步mvc拦截机制做好,第二步判断金额是否一致,生成订单号,扣减库存,最后返回订单编号和金额给权聪调起支付
         * 支付成功的回调会进入到支付宝回调,支付宝回调生成用户每期返还回去的钱,规则是12期,金额每个月1号凌晨3点进行结算,
         * 以回调那边为准,使用rabbitMQ做异步解耦削峰控流,异步回调那边做,有16800也有49800元,支付调用之前的支付接口进行支付返回就行
         * 回调处理好业务逻辑,继续使用到消息
         */
        assert BlankUtil.isNotEmpty(param);
        IErrorCode errorCode = validationCode(getById(Long.valueOf(param.getPartnerPdtId())), param);
        if (BlankUtil.isEmpty(errorCode)) {
            OmsOrder order = param.getParam(UserUtil.getCurrentUser());
            OmsOrderItem orderItem = param.getParam(order);
            boolean b = partnerProductMapper.insertOrder(order, orderItem);
            log.info("合伙人订单生成:{},订单id:{}", b, order.getOrderId());
            return b ? CommonResult.success(new GenerateOrderBO(order.getOrderSn(), param.getTotalPrice().toString())) : CommonResult.failed(CommonResultCode.FAILED);
        }
        return CommonResult.failed(errorCode);
    }

    private IErrorCode validationCode(PmsPartnerProduct partnerProduct, PartnerGenerateOrderParam param) {
        IErrorCode errorCode = null;
        if (BlankUtil.isEmpty(partnerProduct)) {
            // 对象为空
            errorCode = BusinessErrorCode.NOT_PARTNER_PDT;
        } else if (!partnerProduct.getPublishStatus()) {
            //下线
            errorCode = BusinessErrorCode.PARTNER_PDT_STATUS_FALSE;
        } else if (!param.getTotalPrice().equals(partnerProduct.getTotalPrice() * param.getQuantity())) {
            // 金额不一致
            errorCode = BusinessErrorCode.ERR_PRODUCT_PRICE;
        } else if (!param.getEarnestMoney().equals(partnerProduct.getEarnestMoney())) {
            // 保证金
            errorCode = BusinessErrorCode.NOT_PARTNER_EARNEST_MONEY;
        } else if (!param.getPartnerPrice().equals(partnerProduct.getPartnerPrice())) {
            // 金额
            errorCode = BusinessErrorCode.NOT_PARTNER_PRICE;
        }
        return errorCode;
    }
}
