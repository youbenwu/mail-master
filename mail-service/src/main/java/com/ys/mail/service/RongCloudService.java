package com.ys.mail.service;


import com.ys.mail.exception.ApiException;
import com.ys.mail.model.admin.vo.UserImInfoVO;
import io.rong.models.Result;
import io.rong.models.message.MessageModel;
import io.rong.models.response.ResponseResult;

import java.util.List;

/**
 * @Desc - 融云Service，封装第三方统一业务
 * - 通用Service层：可以将admin portal公共、重复的服务提取出来
 * @Author CRH
 * @Create 2022-01-13 15:30
 */
public interface RongCloudService {

    String register(Long userId, String name, String portrait, String forcedRefresh) throws ApiException;

    Result updateUser(Long userId, String name, String portrait) throws ApiException;

    // 用户通知
    ResponseResult send(MessageModel message);

    String getAppKey();

    /**
     * 获取后台系统客服，暂时返回一条记录
     * TODO:后期可以做成返回较为空闲的客服，通过消息的未读数、在线状态等计算
     *
     * @return 客服列表
     */
    List<UserImInfoVO> getStaff();

}
