package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 快递公司编号表
 * </p>
 *
 * @author 070
 * @since 2021-12-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysKdCode对象", description="快递公司编号表")
public class SysKdCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "kd_id", type = IdType.AUTO)
    private Integer kdId;

    @ApiModelProperty(value = "快递公司名称")
    private String kdName;

    @ApiModelProperty(value = "快递公司编码")
    private String kdCode;


}
