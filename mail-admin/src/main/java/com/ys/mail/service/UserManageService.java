package com.ys.mail.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.UmsUserQuery;
import com.ys.mail.model.admin.vo.UmsUserBlackListVO;
import com.ys.mail.model.admin.vo.UserImInfoVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * <p>
 * app用户表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-09
 */
public interface UserManageService extends IService<UmsUser> {

    /**
     * 获取分页信息
     *
     * @param query 查询条件
     * @return 分页结果
     */
    CommonResult<IPage<UmsUserBlackListVO>> getPage(UmsUserQuery query);

    /**
     * 获取用户聊天的基本信息
     *
     * @param userIds 用户ID列表
     * @return 结果
     */
    CommonResult<List<UserImInfoVO>> getUserImInfo(List<String> userIds);

    /**
     * 导出平台全量用户数据
     *
     * @param condition 条件，false->简单版，true->详细版
     * @param response  响应
     */
    void exportExcel(boolean condition, HttpServletResponse response);

    /**
     * 导出单个用户明细数据
     *
     * @param userId   用户ID
     * @param response 响应
     */
    void exportUserDetailsExcel(Long userId, HttpServletResponse response);
}
