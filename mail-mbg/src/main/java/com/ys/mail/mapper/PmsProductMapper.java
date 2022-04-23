package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.PmsProduct;
import com.ys.mail.model.admin.query.PmsProductQuery;
import com.ys.mail.model.dto.ProductCollectDTO;
import com.ys.mail.model.dto.ProductInfoDTO;
import com.ys.mail.model.param.ProductParam;
import com.ys.mail.model.po.BuyProductPO;
import com.ys.mail.model.po.ProductPO;
import com.ys.mail.model.vo.PmsProductVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品信息表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-10
 */
@Mapper
public interface PmsProductMapper extends BaseMapper<PmsProduct> {

    /**
     * 查找大尾狐列表
     *
     * @param productId 商品id
     * @return 返回值
     */
    List<PmsProduct> selectAllProduct(@Param("productId") Long productId);

    /**
     * 首页服饰,珠宝等进入的大牌精选等
     *
     * @param pdtCgyId 商品参数id
     * @return 返回值
     */
    List<PmsProduct> selectProductPick(@Param("pdtCgyId") Long pdtCgyId);

    /**
     * 精选,精致,穿搭,生活
     *
     * @param homeProductType 参数验证
     * @param productId       商品id
     * @return 返回值
     */
    List<PmsProduct> selectHomeProductType(@Param("productId") Long productId, @Param("homeProductType") Integer homeProductType);

    /**
     * 新增
     *
     * @param pdtCollectId
     * @param userId
     * @param productId
     * @return
     */
    boolean saveCollectProduct(@Param("pdtCollectId") Long pdtCollectId, @Param("userId") Long userId, @Param("productId") Long productId);

    /**
     * 删除收藏
     *
     * @param pdtCollectId 收藏主键id
     * @return 返回值
     */
    boolean delCollectProduct(@Param("pdtCollectId") Long pdtCollectId);

    /**
     * 商品详情
     *
     * @param productId 商品id
     * @param flag      参数值
     * @return 返回值
     */
    ProductInfoDTO selectProductInfo(@Param("productId") Long productId);

    /**
     * 商品收藏列表
     *
     * @param userId       用户id
     * @param pdtCollectId 翻页id
     * @return 返回值
     */
    List<ProductCollectDTO> selectAllCollectProduct(@Param("userId") Long userId, @Param("pdtCollectId") Long pdtCollectId);

    /**
     * 批量删除收藏的商品
     *
     * @param ids 集合
     * @return 返回值
     */
    boolean batchCollectDel(@Param("ids") List<Long> ids);

    /**
     * 获取购买商品的一些基本信息
     *
     * @param skuStockId skuId
     * @param quantity   数量
     * @param flag       值
     * @return 返回值
     */
    BuyProductPO selectBuyProduct(@Param("skuStockId") Long skuStockId, @Param("quantity") Integer quantity, @Param("flag") Boolean flag);

    Page<PmsProductVO> getPage(Page<PmsProductVO> page, @Param("query") PmsProductQuery query);

    /**
     * 为你精选,猜你喜欢
     *
     * @param productId 翻页id
     * @param pdtCgyId  类型id
     * @return 返回集合
     */
    List<PmsProduct> selectHandpickProduct(@Param("productId") Long productId, @Param("pdtCgyId") Long pdtCgyId);

    /**
     * 查询当前产品和用户是否收藏
     *
     * @param productId 产品id
     * @param userId    用户id
     * @return 返回值
     */
    Long selectByUserIdOrPdtId(@Param("productId") Long productId, @Param("userId") Long userId);

    List<PmsProduct> selectProductOfHandpickRecommend(@Param("productId") Long productId, @Param("pageSize") Integer pageSize);

    /**
     * @param productId 1
     * @param quantity  1
     * @return 1
     */
    boolean updateSale(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    boolean updateReduce(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    /**
     * 连表删除
     *
     * @param productId 产品id
     * @return 返回值
     */
    boolean delById(@Param("productId") Long productId);

    List<PmsProduct> partnerProduct(@Param("partnerId") Long partnerId, @Param("productId") Long productId, @Param("pageSize") Integer pageSize);

    /**
     * 根据订单id修改销量,暂时没有支持多数量购买,支付宝这边也会出现风控,故只有一件,后面需要改动的时候去销量相减
     *
     * @param orderId 订单id
     * @return 返回值
     */
    boolean updateByOrderId(@Param("orderId") Long orderId);

    /**
     * 查询精致-生活等
     *
     * @param param 传参实体
     * @return 返回值
     */
    List<ProductPO> selectAllPdtType(@Param("param") ProductParam param);

    List<PmsProduct> selectMebs();

    /**
     * 详情
     *
     * @param productId
     * @param flag
     * @return
     */
    ProductInfoDTO selectPdtInfo(@Param("productId") Long productId, @Param("flag") Boolean flag);
}
