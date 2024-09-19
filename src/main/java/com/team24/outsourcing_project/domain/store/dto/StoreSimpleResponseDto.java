package com.team24.outsourcing_project.domain.store.dto;

import com.team24.outsourcing_project.domain.menu.entity.Menu;

import java.sql.Time;
import java.util.List;

public class StoreSimpleResponseDto {
    private final Long userId;
    private final String name;
    private final int minOrderPrice;
    private final Time openTime;
    private final Time closeTime;

    private StoreSimpleResponseDto(Long userId, String name, int minOrderPrice, Time openTime, Time closeTime)
    {
        this.userId = userId;
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
    public static StoreSimpleResponseDto of(Long userId, String name, int minOrderPrice, Time openTime, Time closeTime)
    {
        return new StoreSimpleResponseDto(userId, name, minOrderPrice, openTime, closeTime);
    }
}
