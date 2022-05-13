package com.ys.mail.domain;

/**
 * 人脸识别抽象工厂类
 * @author ghdhj
 */
public abstract class FaceProvider {

    /**
     * 获取认证的基本信息
     * @param obj 参数
     * @return 返回值
     * @param <T> 返回值
     */
    public abstract <T> T authorizationCode(Object obj);
}
