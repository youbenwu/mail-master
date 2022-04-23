package com.ys.mail.component.rongcloud;

import com.ys.mail.config.RongCloudConfig;
import com.ys.mail.exception.ApiException;
import com.ys.mail.util.BlankUtil;
import io.rong.RongCloud;
import io.rong.methods.message._private.Private;
import io.rong.methods.message.system.MsgSystem;
import io.rong.methods.user.User;
import io.rong.methods.user.blacklist.Blacklist;
import io.rong.models.Result;
import io.rong.models.message.MessageModel;
import io.rong.models.response.ResponseResult;
import io.rong.models.response.TokenResult;
import io.rong.models.user.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @Desc 融云客户端
 * @Author CRH
 * @Create 2022-01-20 00:50
 */
@Component
public class RongCloudClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RongCloudClient.class);

    @Autowired
    private RongCloudConfig rongCloudConfig;
    private User user;
    private RongCloud rongCloud;
    private Blacklist blackList;
    private Private msgPrivate;
    private MsgSystem system;

    @PostConstruct
    public void postConstruct() {
        String apiUrlStr = this.rongCloudConfig.getApiUrl();
        if (StringUtils.isEmpty(apiUrlStr)) {
            throw new RuntimeException("RongCloud Client Init Exception");
        } else {
            String[] apiUrlArray = apiUrlStr.split(",");
            String mainUrl = apiUrlArray[0].trim();
            if (!mainUrl.startsWith("http://") && !mainUrl.startsWith("https://")) {
                mainUrl = "http://" + mainUrl;
            }

            List<String> backUpUrlList = new ArrayList();
            if (apiUrlArray.length > 1) {
                for (int i = 1; i < apiUrlArray.length; ++i) {
                    String backApiUrl = apiUrlArray[i].trim();
                    if (!backApiUrl.startsWith("http://") && !backApiUrl.startsWith("https://")) {
                        backApiUrl = "http://" + backApiUrl;
                    }

                    backUpUrlList.add(backApiUrl);
                }
            }

            this.rongCloud = RongCloud.getInstance(this.rongCloudConfig.getAppKey(), this.rongCloudConfig.getAppSecret(), mainUrl, backUpUrlList);
            this.user = this.rongCloud.user;
            this.blackList = this.rongCloud.user.blackList;
            this.msgPrivate = this.rongCloud.message.msgPrivate;
            this.system = this.rongCloud.message.system;
        }
    }

    public TokenResult register(String userId, String name, String portrait) throws ApiException {
        LOGGER.info("【RongCloud Server】 - register：{}", userId);
        return RongCloudInvokeTemplate.getData(() -> {
            UserModel u = (new UserModel()).setId(userId).setName(name).setPortrait(BlankUtil.isNotEmpty(portrait) ? portrait : this.rongCloudConfig.getDefaultPortraitUrl());
            return this.user.register(u);
        });
    }

    public Result updateUser(String encodeId, String name, String portrait) throws ApiException {
        return RongCloudInvokeTemplate.getData(() -> {
            UserModel u = (new UserModel()).setId(encodeId).setName(name).setPortrait(portrait);
            return this.user.update(u);
        });
    }

    /**
     * 用户通知
     * 发送系统消息方法（一个用户向一个或多个用户发送系统消息，单条消息最大 128k，会话类型为 SYSTEM。
     * 通过 Server API 单个应用每秒钟最多发送 100 条消息，每次最多同时向 100 人发送，如：一次发送 100 人时，示为 100 条消息
     *
     * @param message 发送消息的消息体
     * @return http 成功返回结果
     */
    public ResponseResult send(MessageModel message) {
        LOGGER.info("【RongCloud Server】 - send ");
        return RongCloudInvokeTemplate.getData(() -> this.system.send(message));
    }

    public String getAppKey() {
        LOGGER.info("【RongCloud Server】 - getAppKey");
        return this.rongCloudConfig.getAppKey();
    }
}
