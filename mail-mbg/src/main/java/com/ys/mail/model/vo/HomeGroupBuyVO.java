package com.ys.mail.model.vo;

import com.ys.mail.model.dto.GroupBuyDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-26 17:46
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HomeGroupBuyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "拼团人数")
    private Integer groupSum;

    @ApiModelProperty(value = "首页拼团商品")
    private List<GroupBuyDTO> groupBuyDTOList;
}
