package com.team24.outsourcing_project.domain.store.dto;

import com.team24.outsourcing_project.domain.menu.entity.Menu;
import lombok.Getter;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@Getter
public class StoreSimpleResponseDto {
    private final Long userId;
    private final String name;
    private final int minOrderPrice;
    private final LocalTime openTime;
    private final LocalTime closeTime;

    private StoreSimpleResponseDto(Long userId, String name, int minOrderPrice, LocalTime openTime, LocalTime closeTime) {
        this.userId = userId;
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public static StoreSimpleResponseDto of(Long userId, String name, int minOrderPrice, LocalTime openTime, LocalTime closeTime) {
        return new StoreSimpleResponseDto(userId, name, minOrderPrice, openTime, closeTime);
    }
}
