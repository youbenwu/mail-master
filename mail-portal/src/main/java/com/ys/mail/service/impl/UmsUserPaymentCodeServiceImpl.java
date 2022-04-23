package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.entity.UmsUserPaymentCode;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.mapper.UmsUserPaymentCodeMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.UserTemPayCodeParam;
import com.ys.mail.service.RedisService;
import com.ys.mail.service.UmsUserPaymentCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.util.AesUtil;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户支付密码表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-12-08
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UmsUserPaymentCodeServiceImpl extends ServiceImpl<UmsUserPaymentCodeMapper, UmsUserPaymentCode> implements UmsUserPaymentCodeService {

   /* @Value("${redis.database}")
    private String redisDatabase;
    @Value("${redis.key.userVerify}")
    private String userVerify;

    @Autowired
    private RedisService redisService;
    @Autowired
    private UmsUserPaymentCodeMapper umsUserPaymentCodeMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;*/

  /*  public Boolean checkPaymentCode(Long userId){
        int count=count(new QueryWrapper<UmsUserPaymentCode>().eq("user_id",userId));
        return count>0;
    }*/

    /*public Boolean savePaymentCode(Long userId,String paymentCode){
        boolean flag=false;
        UmsUserPaymentCode code=getOne(new QueryWrapper<UmsUserPaymentCode>().eq("user_id",userId));

        // 先对原始密码进行md5化
        String md5Password = DigestUtils.md5DigestAsHex(paymentCode.getBytes());
        // 再进行AES加密
        String aesPassword = AesUtil.aesEncrypt(md5Password);

        if(BlankUtil.isEmpty(code)){
            code=new UmsUserPaymentCode();
            code.setUserId(userId);
            code.setPaymentCode(aesPassword);
            code.setPaymentCodeId(IdWorker.generateId());
            code.setCreateTime(new Date());
            flag=save(code);
        }else {
            code.setPaymentCode(aesPassword);
            code.setUpdateTime(new Date());
            flag=updateById(code);
        }
        return flag;
    }*/

   /* @Override
    public Boolean removePaymentCode(Long userId){
        return remove(new QueryWrapper<UmsUserPaymentCode>().eq("user_id",userId));
    }*/

   /* public String getPaymentCode(Long userId){
        UmsUserPaymentCode code=getOne(new QueryWrapper<UmsUserPaymentCode>().eq("user_id",userId));
        if(BlankUtil.isEmpty(code)) return "";
        return code.getPaymentCode();
    }*/

   /* @Override
    public CommonResult<Boolean> setPaymentCode(UserTemPayCodeParam param){
        // 判断当前随机码是否有效
        UmsUser currentUser = UserUtil.getCurrentUser();
        Long userId = currentUser.getUserId();
        String newPassword = param.getNewPassword();
        if(!newPassword.equals(param.getOldPassword()))
        {
            return CommonResult.failed(CommonResultCode.ERR_NOT_PAY_CODE);
        }else if(!verifyRandomCode(currentUser.getPhone(),param.getRandomCode()))
        {
            return CommonResult.failed(CommonResultCode.ERR_OPERATION_TIMEOUT);
        }else if(checkPaymentCode(userId))
        {
            return CommonResult.failed(CommonResultCode.ERR_IS_PAY_CODE);
        }
        UmsUserPaymentCode paymentCode = new UmsUserPaymentCode(
                IdWorker.generateId(),
                userId,
                passwordEncoder.encode(newPassword));
        return save(paymentCode) ? CommonResult.success(true):CommonResult.failed(false);
    }
*/
   /* @Override
    public Boolean verifyPaymentCode(String paymentCode){
        UmsUser currentUser = UserUtil.getCurrentUser();
        String user_payment_code=getPaymentCode(currentUser.getUserId());
        if(StringUtils.isEmpty(user_payment_code)) return false;
        //对系统保存的支付密码解密
        String current_payment_code=AesUtil.aesDecrypt(user_payment_code);
        String app_payment_code=AesUtil.aesDecrypt(paymentCode);
        return current_payment_code.equals(app_payment_code);
    }*/

   /* @Override
    public CommonResult<Boolean> updatePaymentCode(String oldPaymentCode, String password){
        if(!verifyPaymentCode(oldPaymentCode)) return CommonResult.failed(false);

        UmsUser currentUser = UserUtil.getCurrentUser();

        boolean b = savePaymentCode(currentUser.getUserId(), password);
        return b ? CommonResult.success(true) : CommonResult.failed(false);
    }*/

    /**
     * 判断当前随机码是否有效
     * @param phone 用户电话号码
     * @param randomCode 随机码
     * @return
     */
   /* private boolean verifyRandomCode(String phone,String randomCode){
        String key=redisDatabase + ":" +userVerify+":"+phone;
        if(!redisService.hasKey(key)){
            return false;
        }
        String code= (String) redisService.get(key);
        if(!randomCode.equals(code)){
            return false;
        }
        redisService.del(key);
        return true;
    }*/
}
