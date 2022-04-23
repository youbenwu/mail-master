package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.SmsHomeAdvertise;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.vo.HomePageVO;

import java.util.List;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-09 16:25
 */

public interface SmsHomeAdvertiseService extends IService<SmsHomeAdvertise> {

    /**
     * 获取app端首页轮播图
     * @return 返回集合
     */
    List<SmsHomeAdvertise> getAllAdvertise();

    /**
     * 首页-轮播图,限时秒杀,拼团购
     * @return 返回值
     */
    HomePageVO homePage(Byte cpyType);
}
