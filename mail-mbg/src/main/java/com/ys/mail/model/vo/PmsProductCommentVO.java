package com.ys.mail.model.vo;

import com.ys.mail.entity.PmsProductComment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 商品评价表
 * </p>
 *
 * @author 070
 * @since 2021-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "PmsProductCommentVO对象", description = "商品评论")
public class PmsProductCommentVO extends PmsProductComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String headPortrait;

}
