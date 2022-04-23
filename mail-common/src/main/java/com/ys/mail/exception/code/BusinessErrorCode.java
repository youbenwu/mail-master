package com.ys.mail.exception.code;

/**
 * @version V1.0
 * @Description: 业务异常状态码(400开头)
 * @author: hezhidong
 */
public enum BusinessErrorCode implements IErrorCode {
    // 后三位1开头与商品有关
    RESPONSE_SUCCESS(200, "操作成功"),
    GOODS_NOT_EXIST(400100, "商品不存在"),
    GOODS_STOCK_EMPTY(400101, "商品库存不足"),
    GOODS_STOCK_DEDUCT_FAILED(400102, "商品库存扣减失败"),
    GOODS_STOCK_RESTORE_FAILED(400103, "商品库存还原失败"),
    ORDER_STOCK_FAILED(400104, "订单入库失败"),
    ORDER_CANCEL_FAILED(400105, "取消订单失败"),
    ORDER_COMFIRM_FAILED(400106, "确认订单失败"),
    ORDER_DELETE_FAILED(400107, "删除订单失败"),
    ORDER_OPERATION_RECORD_FAILED(400108, "记录订单操作失败"),
    APPRAISE_CREATE_FAILED(400109, "评价单生成失败"),
    APPRAISE_ORDER_FAILED(400110, "评价订单失败"),
    APPRAISE_ORDER_EXPIRE_FAILED(400111, "超时评价订单失败"),
    REFUND_COMMIT_FAILED(400112, "退款申请提交失败"),
    AFTERSALE_ADD_LOGISTICCODE_FAILED(400113, "填写退货快递单号失败"),
    ERR_PRODUCT_PRICE(400114, "商品价格计算有误"),
    ERR_ALIPAY_AUTH(400115, "支付宝认证异常"),
    ERR_BIND_ALIPAY(400116, "未满24小时再认证支付宝"),
    ERR_NAME_ACCOUNT_ALIPAY(400117, "支付宝账号重复"),
    ERR_NAME_ALIPAY(400118, "支付宝实名姓名不一致"),
    ERR_UNION_E_MSG(400119, "云闪付生成报文异常"),
    ERR_DEPOSIT_MONEY_LENGTH(400120, "提现金额最低0.1元"),
    ERR_ALIPAY_DEPOSIT(400121, "支付宝提现异常,请联系客服"),
    ERR_ORDER_NOTIFY(400122, "回调订单插入异常"),
    ERR_COMMISSION(400123, "保存分佣异常"),

    ORDER_UPDATE_FAILED(400124, "订单更新失败"),
    ORDER_GIFT_FAILED(400125, "礼品兑换失败"),
    ERR_PRODUCT_RE_EXCHANGE(400126, "不能重复兑换礼品"),
    GIFT_NOT_EXIST(400127, "礼品ID不存在"),
    ORDER_NOT_EXIST(400128, "订单不存在"),
    PRODUCT_SKU_NOT_EXIST(400129, "商品SKU不存在"),
    ERR_START_GREATER_END(400130, "开始时间不能大于结束时间"),
    NOT_PDT_CATEGORY(400131, "没有分类信息"),
    NOT_ORDER_LIST(400132, "没有订单信息"),
    PRODUCT_CONFIRMRECEIPT_FAILED(400140, "确认收货失败"),
    PRODUCT_CONFIRMRECEIPT_FAILED_ORDER_ERROR(400141, "确认收货失败-订单状态异常"),

    ERR_DEPOSIT_MONEY_EX_COUNT(400150, "当天提现次数已达上限(由于风控：暂时每天限提%d次)"),
    REVIEW_DEPOSIT_MONEY_EXCEED(400151, "该笔提现已申请后台审核，请稍后查看结果"),
    REVIEW_DEPOSIT_MONEY_MAX(400152, "单笔最大限额%s"),

    REVIEW_EX_TIME_LOSE(400160, "该笔提现申请审核已失效，请明天再重新申请"),
    ERR_DEPOSIT_MONEY_REVIEWING(400161, "该笔提现正在审核中，请稍后查看结果"),
    REVIEW_EX_REFUSED(400162, "该笔提现审核不通过"),
    REVIEW_EX_ERROR(400163, "该笔提现审核失败"),
    REVIEW_EX_COUNT_OVERSIZE(400164, "当天提现审核次数已达上限(每天一次)"),
    REVIEW_EX_TIME_OVERSIZE(400165, "当天申请审核时间已过，需等到明天"),
    REVIEW_EX_PASS(400166, "当天审核已通过并提现，请留意到账信息"),
    REVIEW_MIN_EX_TIME_OVERSIZE(400167, "小额提现时间不在范围之内，暂无法操作"),

    USER_IMAGE_STRING_EXIST(400200, "该人脸已在其它账户被注册过！"),
    USER_IMAGE_STRING_UNREGISTERED(400201, "未注册人脸数据！"),

    NOT_PROMOTION_HOME(400210, "默认首页状态不能修改"),

    NOT_PARTNER_PDT(400220, "合伙人产品不存在"),
    PARTNER_PDT_STATUS_FALSE(400221, "合伙人产品已下线"),
    NOT_PARTNER_EARNEST_MONEY(400222, "合伙人保证金错误"),
    NOT_PARTNER_PRICE(400223, "合伙人金额错误"),
    NOT_ROLE_ONE_USER(400224, "合伙人金额错误"),
    ;

    private int code;

    private String message;

    BusinessErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
