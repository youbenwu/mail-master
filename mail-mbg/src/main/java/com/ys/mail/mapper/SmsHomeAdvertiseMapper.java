package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.SmsHomeAdvertise;
import com.ys.mail.model.admin.query.HomeAdvertiseQuery;
import com.ys.mail.model.vo.HomePageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 首页轮播广告表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-03
 */
@Mapper
public interface SmsHomeAdvertiseMapper extends BaseMapper<SmsHomeAdvertise> {
    /**
     * 查询列表
     * @param page 分页参数
     * @param query 查询对象
     * @return 返回值
     */
    Page<SmsHomeAdvertise> selectList(@Param("page") Page<SmsHomeAdvertise> page, @Param("query") HomeAdvertiseQuery query);

    /**
     * app端随机从数据库中查出5条首页轮播图
     * @return
     */
    List<SmsHomeAdvertise> selectAllAdvertise();

}
