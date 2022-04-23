package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.PmsPartnerProduct;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.bo.GenerateOrderBO;
import com.ys.mail.model.dto.PartnerProductDTO;
import com.ys.mail.model.param.PartnerGenerateOrderParam;
import com.ys.mail.model.po.PartnerProductPO;

import java.util.List;

/**
 * @author ghdhj
 */
public interface PmsPartnerProductService extends IService<PmsPartnerProduct> {
    /**
     * 查询单个或多个
     *
     * @param more         参数
     * @param partnerPdtId id
     * @return 返回值
     */
    List<PartnerProductDTO> list(Byte more, String partnerPdtId);

    /**
     * 合伙人详情
     *
     * @param partnerPdtId 主键id
     * @return 返回值
     */
    PartnerProductPO info(String partnerPdtId);

    /**
     * 合伙人立即购买页面
     *
     * @param partnerPdtId id
     * @return 返回值
     */
    PartnerProductDTO buy(String partnerPdtId);

    /**
     * 生成订单
     *
     * @param param 实体
     * @return 返回值
     */
    CommonResult<GenerateOrderBO> generateOrder(PartnerGenerateOrderParam param);
}
