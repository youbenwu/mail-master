package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.PmsProductComment;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.ProductCommentParam;
import com.ys.mail.model.vo.PmsProductCommentVO;

import java.util.List;

/**
 * <p>
 * 商品评价表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-18
 */
public interface PmsProductCommentService extends IService<PmsProductComment> {
    /**
     * 商品评价
     * @param param 实体body
     * @return 返回值
     */
    CommonResult<Boolean> saveComment(List<ProductCommentParam> param,Long orderId);

    /**
     * 获取商品评论，带分页
     * @param productId 该商品ID
     * @param pdtCommentId 评论ID，用于分页
     * @param pageSize 分页大小
     * @return 评论列表
     */
    List<PmsProductCommentVO> getProductComment(String productId, Long pdtCommentId, Integer pageSize);

    Integer getProductCommentCount(String productId);
}
