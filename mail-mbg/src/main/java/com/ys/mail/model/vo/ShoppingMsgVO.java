package com.ys.mail.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ghdhj
 */
@Data
public class ShoppingMsgVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "快购价")
    private Long productIdPrice;

    @ApiModelProperty(value = "商品图片")
    private String productPic;

    @ApiModelProperty(value = "用户名称")
    private String nickName;

    @ApiModelProperty(value = "抢到商品的用户名称")
    private String newNickname;

    @ApiModelProperty(value = "翻页id,首次传null,每次传最后一个id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long histroyId;

    @ApiModelProperty(value = "每次秒杀的用户头像")
    private String userPic;

    @ApiModelProperty(value = "持有商品的用户头像")
    private String newUserPic;

    @ApiModelProperty(value = "商品名称")
    private String productName;
}
