package com.ys.mail.exception.code;

/**
 * @Description:
 * @author: hezhidong
 */
public enum CommonResultCode implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(400, "操作失败"),

    FORBIDDEN(401, "没有相关权限"),
    DATA_ACCESS_FAILED(402, "数据获取失败"),
    FEIGN_FAILED(403, "feign调用失败"),
    SERVER_ERROR(500, "服务器错误"),
    UNAUTHORIZED(406, "暂未登录或token已经过期"),
    FREQUENT_REQUEST(407, "频繁请求"),
    NOT_SENIOR_USER(408, "不是高级用户"),
    ILLEGAL_REQUEST(409, "非法请求"),

    VALIDATE_FAILED(1001, "参数检验失败"),
    REQUEST_PARAM_ISEMPTY(1002, "请求参数不允许为空"),
    ERR_INTERFACE_PARAM(1003, "接口参数异常"),

    URL_FAILED(10001, "url请求参数错误"),
    ERR_AUTH_CODE(10002, "验证码错误"),
    ERR_IS_NAME(10003, "用户名不一致"),
    ERR_USER_REAL(10004, "用户未实名"),
    ERR_LOGIN_FAILURE(10005, "登录已失效"),
    ERR_REQUEST_METHOD(10006, "请求方式错误"),
    ERR_HTTP_MEDIA_TYPE(10007, "请求体type类型错误"),
    ERR_MISSING_SERVLET(10008, "请求参数格式错误"),
    ERR_ADDRESS_DEFAULT(10009, "默认收货地址不能修改"),
    ERR_NOT_READABLE(10010, "json传参异常"),
    ERR_USER_DEPOSIT(10011, "余额不足"),
    ERR_READ_FILE(10012, "读取文件异常"),
    ERR_USER_PRODUCT_COLLECT(10013, "用户已收藏该商品"),
    ERR_OPERATION_TIMEOUT(10014, "操作超时"),
    ERR_IS_PAY_CODE(10015, "已存在支付密码"),
    ERR_NOT_PAY_CODE(10016, "支付密码输入不一致"),
    ERR_TEM_PAY_CODE(10017, "请设置支付密码"),
    ERR_APP(10018, "后台异常"),

    ERR_REPAYMENT_CODE(10019, "无需重复支付"),
    ERR_USER_NOT_SENIOR_ROLE(10020, "不是高级用户"),
    ERR_USER_NON_PAYMENT(10021, "未付款"),

    ERR_DUPLICATE_KEY(10022, "重复参数"),

    ERROR_20001(20001, "用户不存在"),
    ERROR_20002(20002, "用户为空"),
    ERROR_20003(20003, "订单不存在"),

    ERR_BAD_PADDING(20004, "订单失效"),
    ERR_PAY_GIFT(20005, "礼品订单支付异常"),
    ERR_DATABASE(20006, "数据库操作异常"),
    ERR_ORDER_STATUS(20007, "订单状态异常"),
    ERR_SIGN_MISTAKE(20008, "加密错误"),
    ERR_ORDER_TIMEOUT(20009, "订单超时"),
    ;

    private int code;

    private String message;

    CommonResultCode(int code, String message) {
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
