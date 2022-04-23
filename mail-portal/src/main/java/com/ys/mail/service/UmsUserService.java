package com.ys.mail.service;

import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.*;
import com.ys.mail.model.vo.UserInviteDataVO;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <p>
 * app用户表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-09
 */
public interface UmsUserService extends IService<UmsUser> {

    /**
     * 根据用户手机号查询用户的基本信息
     *
     * @param phone 用户手机号
     * @return 返回信息
     */
    UserDetails loadBySecurity(String phone);

    /**
     * 通过手机号获取验证码
     *
     * @param phone 手机号
     * @return 返回值
     */
    CommonResult<String> getAuthCode(String phone, Byte type);

    /**
     * 注册用户账号
     *
     * @param phone    手机号码
     * @param authCode 验证码
     * @return 返回值
     */
    CommonResult<String> userRegister(String phone, String authCode, Long parentId);

    /**
     * 一键登录
     *
     * @param param 对象
     * @return 返回值
     * @throws Exception 抛出异常
     */
    CommonResult<String> userOauth(OauthParam param) throws Exception;

    /**
     * 呼啦兔一键登录
     *
     * @param param 对象
     * @return 返回值
     * @throws Exception 抛出异常
     */
    CommonResult<String> hulatuUserOauth(OauthParam param) throws Exception;

    /**
     * 验证手机号
     *
     * @param phone    手机号
     * @param authCode 验证码
     * @return 返回值
     */
    CommonResult<Boolean> verifyPhone(String phone, String authCode);

    /**
     * 更改手机号
     *
     * @param phone 手机号
     * @return 返回值
     */
    CommonResult<Boolean> changePhone(String phone);

    /**
     * 更换用户昵称
     *
     * @param nickname 昵称
     * @return 返回值
     */
    CommonResult<Boolean> changeNickname(String nickname);

    /**
     * 更换用户头像
     *
     * @param headPortrait 头像地址
     * @return 返回值
     */
    CommonResult<Boolean> changeHeadPortrait(String headPortrait);

    /**
     * 安全验证手机号
     *
     * @param phone    手机号
     * @param authCode 验证码
     * @return 随机码
     */
    CommonResult<String> securityVerifyPhone(String phone, String authCode);

    /*  *//**
     * 删除支付密码
     * @param phone 手机号
     * @param authCode 验证码
     * @return 返回值
     *//*
    CommonResult<Boolean> removePaymentCode(String phone, String authCode);*/

    /**
     * 用户绑定支付宝
     *
     * @param param 实体
     * @return 返回值
     */
    CommonResult<Boolean> bindAlipay(BindAlipayParam param);

    /**
     * 用户提现接口
     *
     * @param param 实体对象
     * @return 返回值
     */
    CommonResult<String> depositAlipay(DepositAlipayParam param) throws AlipayApiException;

    /**
     * 根据用户roleId获取用户信息
     *
     * @param roleId 0普通用户用户,1高级用户,2系统用户
     * @return 用户详情
     */
    UserDetails selectUserByRoleId(Integer roleId);

    /**
     * 设置支付密码
     *
     * @param param 实体对象
     * @return 返回值
     */
    CommonResult<Boolean> setPaymentCode(UserTemPayCodeParam param);

    /**
     * 个人资料
     *
     * @return 返回值
     */
    UmsUser info();

    /**
     * 验证人脸数据是否有效
     *
     * @param userImageString 人脸数据
     * @return bool
     */
    CommonResult<Boolean> verifyFace(String userImageString);

    /**
     * 人脸识别安卓调用sdk
     *
     * @param param 参数实体
     * @return 返回值
     */
    CommonResult<Object> callVerifyFace(VerifyFaceParam param);

    /**
     * 查询用户的邀请数据明细
     *
     * @param userId   分页的用户ID
     * @param pageSize 分页大小
     * @return 结果
     */
    UserInviteDataVO getUserInviteDataList(String userId, Integer pageSize);
}
