package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.UmsBankParam;
import com.ys.mail.service.UmsBankService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.service.UserCacheService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ys.mail.mapper.UmsBankMapper;
import com.ys.mail.entity.UmsBank;

import java.util.List;

/**
 * <p>
 * 用户银行卡表,建议后台转1分审核是否成功 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-22
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UmsBankServiceImpl extends ServiceImpl<UmsBankMapper, UmsBank> implements UmsBankService {

    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private UmsBankMapper bankMapper;

    @Override
    public CommonResult<Boolean> saveBank(UmsBankParam param) {
        // TODO 先验证验证码码是否正确,不用Optional类处理,降低性能
        String code = userCacheService.getAuthCode(param.getBankPhone());
        if(!param.getAuthCode().equals(code)){
            return CommonResult.failed("验证码错误");
        }
        UmsBank umsBank = getOne(new QueryWrapper<UmsBank>().eq("bank_card", param.getBankCard()));
        if(!BlankUtil.isEmpty(umsBank)){
            return CommonResult.failed("请不要添加重复的银行卡");
        }
        UmsBank bank = new UmsBank();
        BeanUtils.copyProperties(param,bank);
        bank.setBankId(IdWorker.generateId());
        bank.setUserId(UserUtil.getCurrentUser().getUserId());
        boolean save = save(bank);

        return save ? CommonResult.success(true) : CommonResult.failed(false);
    }


    @Override
    public List<UmsBank> getByUserId() {
        QueryWrapper<UmsBank> queryWrapper = new QueryWrapper<UmsBank>().eq("user_id",UserUtil.getCurrentUser().getUserId());
        return bankMapper.selectList(queryWrapper);
    }

}
