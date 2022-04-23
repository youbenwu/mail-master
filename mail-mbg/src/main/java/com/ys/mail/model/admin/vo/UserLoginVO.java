package com.ys.mail.model.admin.vo;

import com.ys.mail.model.admin.tree.PcMenuTree;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author DT
 * @version 1.0
 * @date 2021-10-20 15:03
 */
@Data
public class UserLoginVO implements Serializable {

    @ApiModelProperty(value = "用户登录后返回的token")
    private String token;

    @ApiModelProperty(value = "用户的权限集合")
    private List<PcMenuTree> menuTrees;

    @ApiModelProperty(value = "融云Token")
    private String rongCloudToken;

    @ApiModelProperty(value = "融云AppKey")
    private String rongCloudAppKey;

    @ApiModelProperty(value = "COS的图片最新存储路径，前缀")
    private String cosFilePath;

    private UserImInfoVO userInfo;
}
