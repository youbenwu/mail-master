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
 * 省市区数据字典表
 * </p>
 *
 * @author 070
 * @since 2021-12-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysDistrict对象", description="省市区数据字典表")
public class SysDistrict implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "dis_id", type = IdType.INPUT)
    private Long disId;

    @ApiModelProperty(value = "名称")
    private String disName;

    @ApiModelProperty(value = "父级id")
    private Long parentId;

    @ApiModelProperty(value = "前缀英文")
    private String initial;

    @ApiModelProperty(value = "缩写英文")
    private String initials;

    @ApiModelProperty(value = "拼音")
    private String pinyin;

    @ApiModelProperty(value = "少数民族,默认空就是汉族")
    private String extra;

    @ApiModelProperty(value = "简称,省,市等")
    private String suffix;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "直辖区对应的编码")
    private String areaCode;

    @ApiModelProperty(value = "排序")
    private Integer sort;


}
