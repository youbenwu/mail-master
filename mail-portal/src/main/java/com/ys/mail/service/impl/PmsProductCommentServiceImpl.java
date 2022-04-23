package com.ys.mail.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alipay.api.domain.OrderItem;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.OmsOrder;
import com.ys.mail.entity.OmsOrderItem;
import com.ys.mail.entity.PmsProductComment;
import com.ys.mail.exception.ApiException;
import com.ys.mail.mapper.PmsProductCommentMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.ProductCommentParam;
import com.ys.mail.model.vo.PmsProductCommentVO;
import com.ys.mail.service.OmsOrderItemService;
import com.ys.mail.service.OmsOrderService;
import com.ys.mail.service.PmsProductCommentService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商品评价表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-18
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class PmsProductCommentServiceImpl extends ServiceImpl<PmsProductCommentMapper, PmsProductComment> implements PmsProductCommentService {

    @Autowired
    private PmsProductCommentMapper productCommentMapper;

    @Autowired
    private OmsOrderService omsOrderService;

    @Autowired
    private OmsOrderItemService omsOrderItemService;

    /**
     * 添加商品评论
     * @param param 实体body
     * @return bool
     */
    @Override
    public CommonResult<Boolean> saveComment(List<ProductCommentParam> param,Long orderId) {
        // 1.添加商品评论前需要校验该订单的收货状态是否为已确认（is_confirm_status = 1）与是否已评价（is_appraise = 1）
        OmsOrder omsOrder = omsOrderService.getById(orderId);
        boolean flag;
        if(CollectionUtils.isEmpty(param)){
            return CommonResult.failed(false);
        }
        if (BeanUtil.isEmpty(omsOrder)) return CommonResult.failed(false);
        if (7 != omsOrder.getOrderStatus()) return CommonResult.failed(false);
        for(int i=0;i<param.size();i++){
            // 2.添加评论
            PmsProductComment productComment = new PmsProductComment();
            BeanUtils.copyProperties(param.get(i), productComment);
            productComment.setProductId(Long.valueOf(param.get(i).getProductId()));
            productComment.setUserId(UserUtil.getCurrentUser().getUserId());
            productComment.setPdtCommentId(IdWorker.generateId());
            flag = this.saveOrUpdate(productComment);
            if (!flag) throw new ApiException("商品评价失败"); // 异常回滚

            OmsOrderItem orderItem = omsOrderItemService.getById(param.get(i).getOrderItemId());
            if(BlankUtil.isNotEmpty(orderItem)&&orderItem.getAppraise()==false){
                // 2.更新商品评论
                orderItem.setAppraise(true);
                flag = omsOrderItemService.saveOrUpdate(orderItem);
                if (!flag) throw new ApiException("订单商品更新失败"); // 异常回滚
            }
        }
        if(BlankUtil.isNotEmpty(omsOrder)){
            // 3.添加评论成功后，更新该订单的评价时间、评价状态、订单状态为已完成（comment_time、is_appraise = 1、order_status= 3）
            OmsOrder order = new OmsOrder();
            order.setOrderId(Long.valueOf(omsOrder.getOrderId()));
            order.setAppraise(true);
            order.setCommentTime(new Date());
            order.setOrderStatus(3);
            flag = omsOrderService.saveOrUpdate(order);
            if (!flag) throw new ApiException("订单更新失败"); // 异常回滚
        }
        // 成功返回
        return CommonResult.success(true);
    }

    @Override
    public List<PmsProductCommentVO> getProductComment(String productId, Long pdtCommentId, Integer pageSize) {
        return productCommentMapper.selectProductComment(Long.valueOf(productId), pdtCommentId, pageSize);
    }

    @Override
    public Integer getProductCommentCount(String productId) {
        QueryWrapper<PmsProductComment> wrapper = new QueryWrapper<>();
        wrapper.select("pdt_comment_id")
                .eq("product_id", productId);
        return this.count(wrapper);
    }
}
