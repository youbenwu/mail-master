package com.ys.mail.entity;

//import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.naming.Name;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 商品信息表
 * </p>
 *
 * @author 070
 * @since 2021-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PmsProductExcel对象", description="商品信息Excel表")
public class PmsProductExcel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "产品图片")
    @ExcelProperty("产品图片")
    private String pic;

    @ApiModelProperty(value = "型号")
    @ExcelProperty("型号")
    private String productSn;

    @ApiModelProperty(value = "名称")
    @ExcelProperty("名称")
    private String productName;

    @ApiModelProperty(value = "规格名称")
    @ExcelProperty("规格名称")
    private String specificationsName;

    @ApiModelProperty(value = "规格参数")
    @ExcelProperty("规格参数")
    private String specParameters;

    @ApiModelProperty(value = "价格,使用整数类型Long,1分就是1,100就是1元")
    @ExcelProperty("零售价")
    private int price;

    @ApiModelProperty(value = "一级分类\n")
    @ExcelProperty("一级分类\n")
    private String primaryClassification;

    @ApiModelProperty(value = "二级分类\n")
    @ExcelProperty("二级分类\n")
    private String secondaryClassification;

    @ApiModelProperty(value = "品牌")
    @ExcelProperty("品牌")
    private String brand;

    @ApiModelProperty(value = "产品分类名称")
    @ExcelProperty("产品分类名称")
    private String ProductCategoryName;

    @ApiModelProperty(value = "库存")
    @ExcelProperty("库存")
    private Integer stock;

    @ApiModelProperty(value = "库存预警值")
    @ExcelProperty("库存预警值")
    private Integer lowStock;

}
