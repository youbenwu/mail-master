package com.ys.mail.model.dto;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liky
 * @date 2022/1/5 9:44
 * @description 临时工
 */
public class TemporaryWorkers {
    private Long       parentId;
    private List<Long> userIds;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        if(StringUtils.isNotBlank(userIds)) {
            String[] split = userIds.split(",");
            this.userIds = Arrays.stream(split).map(Long::parseLong).collect(Collectors.toList());
        }else {
            this.userIds = new ArrayList<>(0);
        }
    }
}
