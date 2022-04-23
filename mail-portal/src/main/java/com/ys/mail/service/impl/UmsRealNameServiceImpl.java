package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.mail.entity.UmsRealName;
import com.ys.mail.mapper.UmsRealNameMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.UmsRealNameService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.util.UserUtil;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 实名认证表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-12-17
 */
@Service
public class UmsRealNameServiceImpl extends ServiceImpl<UmsRealNameMapper, UmsRealName> implements UmsRealNameService {

    private UmsRealNameMapper umsRealNameMapper;
    @Override
    public CommonResult<Boolean> updateGifeState() {
        QueryWrapper<UmsRealName> ums=new QueryWrapper<>();
        ums.eq("user_id", UserUtil.getCurrentUser().getUserId());
        UmsRealName urm=umsRealNameMapper.selectOne(ums);
        urm.setCardAuthentication(true);
        if(urm!=null){
            umsRealNameMapper.updateById(urm);
        }else {
            CommonResult.failed("更新礼品状态失败");
        }
        return CommonResult.success(true);
    }
}
