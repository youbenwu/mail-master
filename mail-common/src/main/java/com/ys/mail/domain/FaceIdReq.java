package com.ys.mail.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ghdhj
 */
@Data
@Builder
public class FaceIdReq implements Serializable {

    private static final long serialVersionUID = 1L;

    private String webankAppId;
    private String orderNo;
    private String name;
    private String idNo;
    private String userId;
    private String sign;
    private String nonce;
    private String version;
}
