package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 实名认证表
 * </p>
 *
 * @author 070
 * @since 2021-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UmsRealName对象", description="表")
public class UmsRealName implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "real_name_id", type = IdType.INPUT)
    @NotNull(message="realNameId不能为空")
    private Long realNameId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "姓名")
    @NotBlank(message="姓名不能为空")
    private String realName;

    @ApiModelProperty(value = "身份证号码")
    @Pattern(regexp = "(^\\d{8}(0\\d|10|11|12)([0-2]\\d|30|31)\\d{3}$)|(^\\d{6}(18|19|20)\\d{2}(0\\d|10|11|12)([0-2]\\d|30|31)\\d{3}(\\d|X|x)$)",message = "请输入正确的身份证号")
    private String cardNumber;

    @ApiModelProperty(value = "身份证背面")
    @NotBlank(message="身份证背面不能为空")
    private String cardBackImg;

    @ApiModelProperty(value = "身份证正面")
    @NotBlank(message="身份证正面不能为空")
    private String cardPositiveImg;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "领取状态")
    @TableField(fill = FieldFill.INSERT)
    private Boolean cardAuthentication;

}
