package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.EsProduct;
import com.ys.mail.entity.GroupBuy;
import com.ys.mail.model.dto.GroupBuyDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 团购 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-13
 */
@Mapper
public interface EsProductMapper extends BaseMapper<EsProduct> {

    List<EsProduct> getProductEs(Long id);

}
