package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.PmsProduct;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.bo.GenerateOrderBO;
import com.ys.mail.model.dto.BuyProductDTO;
import com.ys.mail.model.dto.ProductCollectDTO;
import com.ys.mail.model.dto.ProductInfoDTO;
import com.ys.mail.model.param.BathGenerateOrderParam;
import com.ys.mail.model.param.ConGenerateOrderParam;
import com.ys.mail.model.param.ProductParam;
import com.ys.mail.model.po.ProductPO;

import java.util.List;

/**
 * <p>
 * 商品信息表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-10
 */
public interface PmsProductService extends IService<PmsProduct> {
    /**
     * 大尾狐自营列表
     *
     * @param productId 商品id
     * @return 返回值
     */
    List<PmsProduct> getAllProduct(Long productId);

    /**
     * 首页服饰,珠宝等进入的大牌精选等
     *
     * @param pdtCgyId 商品参数id
     * @return 返回值
     */
    List<PmsProduct> getProductPick(Long pdtCgyId);

    /**
     * 精选,精致,穿搭,生活
     *
     * @param homeProductType 查找类型
     * @param productId       商品id
     * @return 返回值
     */
    List<PmsProduct> getHomeProductType(Long productId, Integer homeProductType);

    /**
     * 收藏商品和取消收藏
     *
     * @param productId    商品id
     * @param pdtCollectId 收藏id
     * @return 返回值
     */
    CommonResult<String> collectProduct(Long productId, Long pdtCollectId);

    /**
     * 商品详情页
     *
     * @param productId 商品id
     * @param flag      参数值
     * @return 返回值
     */
    ProductInfoDTO getProductInfo(Long productId, Boolean flag);

    /**
     * 商品收藏列表
     *
     * @param pdtCollectId 翻页
     * @return 返回值
     */
    List<ProductCollectDTO> getAllCollectProduct(Long pdtCollectId);

    /**
     * 批量删除收藏的商品
     *
     * @param ids 集合
     * @return 返回true或false
     */
    CommonResult<Boolean> batchCollectDel(List<Long> ids);

    /**
     * 商品立即购买页面
     *
     * @param skuStockId sku_id
     * @param quantity   数量
     * @param flag       值
     * @return 返回值
     */
    BuyProductDTO getBuyProduct(Long skuStockId, Integer quantity, Boolean flag);

    /**
     * 为你精选,猜你喜欢
     *
     * @param productId 翻页id
     * @param pdtCgyId  类型id
     * @return 返回集合
     */
    List<PmsProduct> getHandpickProduct(Long productId, String pdtCgyId);

    /**
     * 生成订单
     *
     * @param param 生成订单实体
     * @return 返回值
     */
    CommonResult<GenerateOrderBO> generateOrder(ConGenerateOrderParam param);

    List<PmsProduct> getProductOfHandpickRecommend(Long productId, Integer pageSize);

    /**
     * 购物车生成订单
     *
     * @param param 生成订单的实体
     * @return 返回值
     */
    CommonResult<Boolean> bathGenerateOrder(BathGenerateOrderParam param);

    /**
     * 查看礼品
     *
     * @return 返回值
     */
    List<PmsProduct> getGift();

    BuyProductDTO getBuyProduct(Long productId);

    List<PmsProduct> partnerProduct(Long productId, Integer pageSize);

    /**
     * 支付成功后增加销量
     *
     * @param orderId 订单id
     * @return 返回值
     */
    boolean updateByOrderId(Long orderId);

    /**
     * 重写精选...生活
     *
     * @param param 实体
     * @return 返回值
     */
    List<ProductPO> searchAllPdtType(ProductParam param);

    List<PmsProduct> selectMebs();
}
