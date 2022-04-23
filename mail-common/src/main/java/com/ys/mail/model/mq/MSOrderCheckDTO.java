package com.ys.mail.model.mq;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author 24sifukj
 * @date 2021/12/29 13:53
 * @description 秒杀订单5分钟检查消息对象
 */
@Data
public class MSOrderCheckDTO implements Serializable {

    private static final long serialVersionUID = -3139319269253518808L;

    private Long orderId;
    private Map<Long,Integer> map;

    public MSOrderCheckDTO(Long orderId, Map<Long, Integer> map) {
        this.orderId = orderId;
        this.map = map;
    }
}
