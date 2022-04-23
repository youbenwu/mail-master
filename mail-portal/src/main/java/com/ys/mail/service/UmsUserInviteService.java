package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.UmsUserInvite;
import com.ys.mail.model.CommonResult;

/**
 * <p>
 * 用户邀请信息表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-22
 */
public interface UmsUserInviteService extends IService<UmsUserInvite> {
    /**
     * 添加用户邀请信息
     *
     * @param umsUserInvite 实体body
     * @return 返回值
     */
    CommonResult<Boolean> addUserInvite(UmsUserInvite umsUserInvite);

    /**
     * 查看用户邀请二维码
     *
     * @param type 公司类型:0->大尾狐,1->呼啦兔
     * @return 生成的二维码路径
     * @throws Exception e
     */
    CommonResult<String> getUserInviteQrCode(String type) throws Exception;


    CommonResult<Boolean> updateUserRole(Long parentId);
}
