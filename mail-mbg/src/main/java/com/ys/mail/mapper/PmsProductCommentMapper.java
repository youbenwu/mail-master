package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.PmsProductComment;
import com.ys.mail.model.vo.PmsProductCommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品评价表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-18
 */
@Mapper
public interface PmsProductCommentMapper extends BaseMapper<PmsProductComment> {
    /**
     * 查询一条商品评论
     *
     * @param productId 商品id
     * @return 返回值
     */
    List<PmsProductComment> selectOneProductId(@Param("productId") Long productId);

    /**
     * 当前商品的所有评论
     *
     * @param productId    商品id
     * @param pdtCommentId 评论id
     * @return 返回值
     */
    List<PmsProductCommentVO> selectProductComment(@Param("productId") Long productId, @Param("pdtCommentId") Long pdtCommentId, @Param("pageSize") Integer pageSize);
}
