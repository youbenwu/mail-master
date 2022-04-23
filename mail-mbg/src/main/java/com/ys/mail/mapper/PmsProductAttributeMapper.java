package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.PmsProductAttribute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品属性参数表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-18
 */
@Mapper
public interface PmsProductAttributeMapper extends BaseMapper<PmsProductAttribute> {
    /**
     * 查询出商品关联的规格属性
     * @param pdtAttributeCgyId id
     * @return 返回值
     */
    List<PmsProductAttribute> selectByPdtAttributeCgyId(@Param("pdtAttributeCgyId") Long pdtAttributeCgyId);


    Page<PmsProductAttribute> get(Page page,@Param("productAttributeName") String productAttributeName);
}
