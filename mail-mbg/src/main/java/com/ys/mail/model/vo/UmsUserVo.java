package com.ys.mail.model.vo;

import com.ys.mail.entity.SmsProductStore;
import com.ys.mail.entity.UmsUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用一句简单的话来描述下该类
 *
 * @author CRH
 * @date 2022-05-09 09:54
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "UmsUserVo")
public class UmsUserVo extends UmsUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户店铺信息")
    private ProductStoreVO storeInfo;

}
