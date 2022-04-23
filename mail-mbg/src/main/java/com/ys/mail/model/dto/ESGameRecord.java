package com.ys.mail.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author 24
 * @date 2021/12/31 10:38
 * @description
 */
@Data
public class ESGameRecord {

    private List<Integer> gameDate;

    private List<GameRecord> earnings;
}
