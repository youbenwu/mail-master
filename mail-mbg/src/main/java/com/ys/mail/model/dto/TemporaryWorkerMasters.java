package com.ys.mail.model.dto;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liky
 * @date 2022/1/5 9:44
 * @description 临时工
 */
public class TemporaryWorkerMasters {
    private Date date;
    private List<Long> flashPromotionId;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Long> getFlashPromotionId() {
        return flashPromotionId;
    }

    public void setFlashPromotionId(String flashPromotionId) {
        if(StringUtils.isNotBlank(flashPromotionId)) {
            String[] split = flashPromotionId.split(",");
            this.flashPromotionId = Arrays.stream(split).map(Long::parseLong).sorted().collect(Collectors.toList());
        }else {
            this.flashPromotionId = new ArrayList<>(0);
        }
    }


}
