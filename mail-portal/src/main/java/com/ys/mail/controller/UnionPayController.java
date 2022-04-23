package com.ys.mail.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.ys.mail.annotation.LocalLockAnn;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.AliBuyProductParam;
import com.ys.mail.model.param.BalanceProductParam;
import com.ys.mail.model.param.BuyProductParam;
import com.ys.mail.service.UnionPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Pattern;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-08 14:51
 */
@RestController
@RequestMapping("/unionPay")
@Validated
@Api(tags = "支付管理")
public class UnionPayController {

    @Autowired
    private UnionPayService unionPayService;

    @ApiOperation("云闪付支付回调地址-DT")
    @PostMapping(value = "/notifyUrl")
    public String notifyUrl(HttpServletRequest request, HttpServletResponse response) {
        // 云闪付支付回调异步处理
        return unionPayService.notifyUrl(request, response);
    }

    @ApiOperation("云闪付商品立即购买")
    @PostMapping(value = "/buyProduct")
    @LocalLockAnn(key = "unionPayBuyProduct:arg[0]")
    public CommonResult<JSONObject> buyProduct(@Validated @RequestBody BuyProductParam param) {
        // 订单id,金额,支付类型,支付的时候在对比一下库存,公司类型也要对比
        return unionPayService.buyProduct(param);
    }

    @ApiOperation("支付宝商品立即购买")
    @PostMapping(value = "/aliPayBuyProduct")
    @LocalLockAnn(key = "alipayCreate:arg[0]")
    public CommonResult<String> aliPayBuyProduct(@Validated @RequestBody AliBuyProductParam param) throws AlipayApiException {
        return unionPayService.aliPayBuyProduct(param);
    }

    @PostMapping(value = "/aliPayNotify")
    public String aliPayNotify(HttpServletRequest request, HttpServletResponse response) {
        // 支付宝回调,成功之后返回"success"
        return unionPayService.aliPayNotify(request, response);
    }

    @ApiOperation("支付宝购买会员")
    @PostMapping(value = "/aliPayBuyUpgrade")
    @LocalLockAnn(key = "aliPayBuyUpgrade:arg[0]")
    public CommonResult<String> aliPayBuyUpgrade(@Validated @RequestBody AliBuyProductParam param) throws AlipayApiException {
        return unionPayService.aliPayBuyUpgrade(param);
    }

    @ApiOperation("余额支付购买")
    @PostMapping(value = "/balancePaymentBuyProduct")
    @LocalLockAnn(key = "unionPayBuyProduct:arg[0]")
    public CommonResult<Boolean> balancePaymentBuyProduct(@Validated @RequestBody BalanceProductParam param) throws AlipayApiException {
        //支付前通过手机短信验证码，来确认是本人操作
        return unionPayService.balancePaymentBuyProduct(param);
    }

    /**
     * 余额支付,防止对数据库的io性能操作过大,这样mysql的io浪费性能非常的大,生成订单不管这笔订单是不是支付,他都是一笔订单,
     * 余额支付的订单传入一个,具体步骤是这样
     * 用户前端操作->生成订单->价格,单号,生成sign->输入支付->验签支付密码和sign,余额够不,余额购的话就开始支付,生成支付订单号,
     * 回调通过消息告诉,每一个生成的order_sn都不能重复,数据库设置索引,订单号做唯一索引不会出现重复,不会出现重复插入,余额支付开启多线程,
     * 使用synchronized做线程安全,金额单中的线程安全,sign->错的那就是false,不影响,生成规则,
     */

    @ApiOperation("余额支付生成签名")
    @PostMapping(value = "/balancePaySign")
    @LocalLockAnn(key = "balancePaySign:arg[0]")
    public CommonResult<String> balancePaySign(@Validated @RequestBody AliBuyProductParam param) throws Exception {
        return unionPayService.balancePaySign(param);
    }

    @ApiOperation("余额发起支付")
    @PostMapping(value = "/balancePay")
    @LocalLockAnn(key = "balancePay:arg[0]")
    public CommonResult<Boolean> balancePay(@RequestParam("sign") String sign,
                                            @RequestParam("payPassword") @Pattern(regexp = "^\\d{6}$", message = "支付密码必须为6位") String payPassword) throws Exception {
        // 参数sign,支付密码
        return unionPayService.balancePay(sign, payPassword);
    }
}
