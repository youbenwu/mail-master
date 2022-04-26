package com.ys.mail.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.SmsProductStore;
import com.ys.mail.model.admin.param.SmsProductStoreParam;
import com.ys.mail.model.admin.query.SmsProductStoreQuery;
import com.ys.mail.model.admin.vo.SmsProductStoreVO;

/**
 * <p>
 * 用户_商品_店铺表 服务类
 * </p>
 *
 * @author 070
 * @since 2022-04-25
 */
public interface SmsProductStoreService extends IService<SmsProductStore> {

    /**
     * 分页条件查询
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<SmsProductStoreVO> getPage(SmsProductStoreQuery query);

    /**
     * 审核店铺状态
     *
     * @param param 审核状态对象
     * @return 审核结果
     */
    boolean updateReviewState(SmsProductStoreParam param);
}
