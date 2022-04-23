package com.ys.mail.component.rongcloud;

import lombok.Data;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-01-13 15:27
 */
@Data
public class ServerApiParams {
    private String traceId;

    private Integer currentUserId;

    private RequestUriInfo requestUriInfo;
}
