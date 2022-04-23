package com.ys.mail.component.rongcloud;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-01-13 15:24
 */
public interface RongCloudCallBack<T> {
    T doInvoker() throws Exception;
}
