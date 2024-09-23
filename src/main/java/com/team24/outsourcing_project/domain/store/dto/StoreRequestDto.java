package com.team24.outsourcing_project.domain.store.dto;

import lombok.Getter;

import java.sql.Time;

@Getter
public class StoreRequestDto {
    private String name;
    private int minOrderPrice;
    private Time openTime;
    private Time closeTime;

}
