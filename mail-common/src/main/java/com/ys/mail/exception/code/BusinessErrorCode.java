package com.ys.mail.exception.code;

/**
 * 业务异常状态码(400开头)
 *
 * @author 007
 * @date 2022-04-19 15:19
 * @since 1.0
 */
public enum BusinessErrorCode implements IErrorCode {
    // 后三位1开头与商品有关
    RESPONSE_SUCCESS(200, "操作成功"),

    NPE_PARAM(400000, "参数异常"),
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
    FLASH_PRODUCT_NO_EXIST(400142, "秒杀商品不存在"),
    FLASH_PRODUCT_NO_BUY(400143, "不能购买自己上架的商品"),

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
    CURRENT_MAX_EX_OVERSIZE(400168, "由于金额较大，本次最高可以提现%s元"),

    USER_IMAGE_STRING_EXIST(400200, "该人脸已在其它账户被注册过！"),
    USER_IMAGE_STRING_UNREGISTERED(400201, "未注册人脸数据！"),

    NOT_PROMOTION_HOME(400210, "默认首页状态不能修改"),

    NOT_PARTNER_PDT(400220, "合伙人产品不存在"),
    PARTNER_PDT_STATUS_FALSE(400221, "合伙人产品已下线"),
    NOT_PARTNER_EARNEST_MONEY(400222, "合伙人保证金错误"),
    NOT_PARTNER_PRICE(400223, "合伙人金额错误"),
    NOT_ROLE_ONE_USER(400224, "合伙人金额错误"),

    STORE_NAME_EXIST(400225, "店铺名称已存在"),
    USER_STORE_EXIST(400226, "只能添加一个店铺"),
    USER_STORE_NO_EXIST(400227, "还未添加店铺信息"),
    USER_STORE_REVIEWING(400228, "店铺信息审核中"),
    USER_STORE_REVIEWED(400229, "当天已经审核过"),
    USER_STORE_NO_PASS(400230, "店铺信息审核不通过"),
    SKU_ID_NULL(400231, "skuId不能为空"),
    ERR_SKU_ID(400232, "请输入正确的skuId"),
    ADDRESS_NULL(400233, "收货地址不存在"),
    GOODS_NOT_NUM_EXIST(400234, "商品不存在或库存不足"),

    ERR_SMS_MISTAKE(400235, "发送短信失败"),
    ERR_PROMOTION_PDT_SALE(400236, "未到过期时间不能退款"),
    PDT_SUPPLY_NOT(400237, "该商品无供应商价格,无法退款!"),
    PDT_UNDER_SUPPLY_PRICE(400238, "当前价低于供应商价格,无法退款!"),

    ERR_PROMOTION_NOT_DATE(400239, "未到时间无法退货退款"),
    ERR_DATE_ILLEGAL(400300, "日期跨度不能小于%d天"),
    ERR_DATE_EXPIRE(400301, "该商品已过期，不能上架"),
    ALLOW_PRODUCT_REFUND(400302, "该商品已经满足退款条件"),

    ERR_KEY_NOT_EXIST(400400, "Path：%s 不存在"),
    ERR_FILE_NAME_FORMAT(400500, "文件名称格式不正确"),
    ERR_FILE_SUFFIX(400501, "文件后缀不正确，格式应为：%s"),
    ERR_FILE_NOT_SUFFIX(400502, "请上传带后缀的文件：%s"),
    ERR_FILE_SIZE_EXCEED(400503, "该类型的文件大小最大限制为 %s"),

    UNFINISHED_APP_UPLOAD(400600, "APP未上传或未上传完成"),
    APP_NOT_GENERATE_QRCODE(400601, "APP未生成二维码，无法发布"),
    ERR_UNIQUE_LOGISTICS(400602, "物流单号重复"),

    ERR_CUSTOMER_NAME_FOUR(400603, "请输入寄件人/收件人手机号后四位!"),
    ERR_KD_BIRD_NULL(400604, "调用快递鸟api为空"),
    ERR_EX_ADDRESS_NULL(400605, "查询自提点为空"),
    ERR_KD_CODE(400606, "没有此快递公司"),

    ERR_SETTING_TYPE_NOT_EXIST(400700, "设置类型不存在，无法发布"),

    CDN_URL_PURGE_QUOTA_EXCEED(400800, "每日CDN的URL刷新配额不足"),
    CDN_PATH_PURGE_QUOTA_EXCEED(400801, "每日CDN的目录刷新配额不足"),
    CDN_URL_PUSH_QUOTA_EXCEED(400802, "每日CDN的URL预热配额不足"),

    ;

    private final int code;

    private final String message;

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
