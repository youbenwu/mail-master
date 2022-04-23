package com.ys.mail.component.rongcloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-01-13 16:32
 */
public class ServerApiParamHolder {

    private final static Logger LOGGER = LoggerFactory.getLogger(ServerApiParamHolder.class);

    private static final ThreadLocal<ServerApiParams> serverApiParamsThreadLocal = new ThreadLocal<>();

    public static ServerApiParams get() {
        return serverApiParamsThreadLocal.get();
    }

    public static void put(ServerApiParams serverApiParams) {
        serverApiParamsThreadLocal.set(serverApiParams);
    }

    public static void remove() {
        serverApiParamsThreadLocal.remove();
    }

    public static String getTraceId() {
        ServerApiParams serverApiParams = serverApiParamsThreadLocal.get();
        if (serverApiParams != null) {
            return serverApiParams.getTraceId();
        }
        return "";
    }

    public static String getURI() {
        ServerApiParams serverApiParams = serverApiParamsThreadLocal.get();
        if (serverApiParams != null) {
            RequestUriInfo requestUriInfo = serverApiParams.getRequestUriInfo();
            if (requestUriInfo != null) {
                return serverApiParams.getRequestUriInfo().getUri();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public static String getEncodedCurrentUserId() {
        ServerApiParams serverApiParams = serverApiParamsThreadLocal.get();
        if (serverApiParams != null) {
            if (serverApiParams.getCurrentUserId() != null) {
                try {
                    return String.valueOf(serverApiParams.getCurrentUserId());
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                    return "";
                }
            }
        }
        return "";
    }
}
