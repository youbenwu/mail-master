package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.SmsProductStore;
import com.ys.mail.model.param.insert.ProductStoreInsertParam;
import com.ys.mail.model.vo.ProductStoreVO;

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
     * 查询个人店铺信息
     *
     * @return 店铺信息
     */
    ProductStoreVO getInfo();

    /**
     * 查询个人店铺信息
     *
     * @return 店铺信息
     */
    SmsProductStore get();

    /**
     * 新增店铺信息
     *
     * @param param 参数
     * @return 操作结果
     */
    boolean addStore(ProductStoreInsertParam param);

    /**
     * 修改店铺信息
     *
     * @param param 参数
     * @return 操作结果
     */
    boolean updateStore(ProductStoreInsertParam param);

    /**
     * 校验店铺名称是否存在
     *
     * @param storeName 店铺名称
     */
    void isExistsStoreName(String storeName);

    /**
     * 获取已经审核过的店铺信息
     *
     * @return 结果
     */
    SmsProductStore getReviewed();
}
