package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 070
 * @since 2021-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PcSysDic对象", description="")
public class PcSysDic implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "字典主键")
    @TableId(value = "sys_dic_id", type = IdType.INPUT)
    private Long sysDicId;

    @ApiModelProperty(value = "字典类型")
    private Integer sysDicType;

    @ApiModelProperty(value = "字典键")
    private String sysDicKey;

    @ApiModelProperty(value = "字典值")
    private String sysDicValue;

    @ApiModelProperty(value = "字典描述")
    private String sysDicDescribe;


}
