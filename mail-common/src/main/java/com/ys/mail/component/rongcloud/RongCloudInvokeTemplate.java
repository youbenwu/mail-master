package com.ys.mail.component.rongcloud;

import com.ys.mail.exception.ApiException;
import com.ys.mail.exception.code.RongCloudErrorCode;
import io.rong.models.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-01-13 15:23
 */
public class RongCloudInvokeTemplate {

    private final static Logger LOGGER = LoggerFactory.getLogger(RongCloudInvokeTemplate.class);

    public static <T extends Result> T getData(RongCloudCallBack<T> action) throws ApiException {

        try {
            Result result = action.doInvoker();
            if (!result.getCode().equals(200)) {
                LOGGER.warn("invoke rongcloud server exception,resultCode={},errorMessage={},traceId={}", result.getCode(), result.getErrorMessage(), ServerApiParamHolder.getTraceId());
            }
            return (T) result;
        } catch (Exception e) {
            LOGGER.error("invoke rongcloud server error：" + e.getMessage() + " ,traceId=" + ServerApiParamHolder.getTraceId(), e);
            throw new ApiException(RongCloudErrorCode.INVOKE_RONG_CLOUD_SERVER_ERROR);
        }
    }
}
