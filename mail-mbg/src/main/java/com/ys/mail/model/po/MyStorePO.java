package com.ys.mail.model.po;

import com.ys.mail.model.dto.MyStoreDTO;
import com.ys.mail.model.vo.ShoppingMsgVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author ghdhj
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyStorePO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "我的店铺产品集合对象")
    private List<MyStoreDTO> storeDtoS;

    @ApiModelProperty(value = "快购消息通知")
    private ShoppingMsgVO msgVO;
}
