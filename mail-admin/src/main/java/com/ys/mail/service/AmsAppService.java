package com.ys.mail.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.AmsApp;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.AmsAppInsertParam;
import com.ys.mail.model.admin.param.AmsAppUpdateParam;
import com.ys.mail.model.admin.query.AppQuery;
import com.ys.mail.model.admin.vo.AmsAppVO;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 070
 * @since 2022-05-13
 */
public interface AmsAppService extends IService<AmsApp> {

    /**
     * 分页查询
     *
     * @param query 条件
     * @return 分页结果
     */
    IPage<AmsAppVO> getPage(AppQuery query);

    /**
     * 添加APP应用
     *
     * @param param 参数
     * @return 是否添加成功
     */
    boolean add(AmsAppInsertParam param);

    /**
     * 更新APP应用
     *
     * @param param 更新参数
     * @return 更新结果
     */
    boolean update(AmsAppUpdateParam param);

    /**
     * 检查应用名称
     *
     * @param name 应用名称
     * @return 是否已存在，false->表示不存在，true->表示已存在
     */
    boolean isExistsName(String name);

    /**
     * 删除APP应用
     *
     * @param id 应用ID
     * @return 删除结果
     */
    boolean delete(Long id);

    /**
     * 应用状态检测
     *
     * @param id 应用ID
     * @return 检测结果
     */
    boolean check(Long id);

    /**
     * 生成二维码
     *
     * @param id 应用ID
     * @return 生成结果
     */
    boolean genQrcode(Long id);

    /**
     * 发布应用
     *
     * @param id 应用ID
     * @return 发布结果
     */
    CommonResult<Boolean> release(Long id);

    /**
     * CDN刷新和预热
     *
     * @param id 应用ID
     * @return 执行结果
     */
    String purgeAndWarmUp(Long id);

    /**
     * 获取已经发布的二维码下载链接信息
     *
     * @return 结果
     */
    Map<String, String> qrcodeInfo();

    /**
     * 根据类型获取APP Logo图标路径
     *
     * @param type 类型
     * @return 相对路径
     */
    String getAppLogoPath(Integer type);
}
