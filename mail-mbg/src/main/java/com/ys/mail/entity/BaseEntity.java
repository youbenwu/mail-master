package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体抽象类 <br/>
 * - 子类默认已经实现序列化了，但是必须额外加上 {serialVersionUID} ，值为1L即可，避免反序列时版本号不一致 <br/>
 * - 子类需要使用 @SuperBuilder ，而不能再使用 @Builder <br/>
 * - 另外被 transient/static 关键字修饰的属性不会被序列化 <br/>
 *
 * @author CRH
 * @date 2022-04-28 10:58
 * @since 1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private T id;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "逻辑删除：0->未删除；1->删除")
    private Integer deleted;

    @ApiModelProperty(value = "备注")
    private String remark;

}
