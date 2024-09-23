package com.team24.outsourcing_project.domain.store.dto;

import com.team24.outsourcing_project.domain.menu.entity.Menu;
import lombok.Getter;

import java.sql.Time;
import java.util.List;

@Getter
public class StoreResponseDto {
    private final Long userId;
    private final String name;
    private final int minOrderPrice;
    private final Time openTime;
    private final Time closeTime;
    private final List<Menu> menuList;


    private StoreResponseDto(Long userId, String name, int minOrderPrice, Time openTime, Time closeTime, List<Menu> menuList)
    {
        this.userId = userId;
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.menuList = menuList;


    }
    public static StoreResponseDto of(Long userId, String name, int minOrderPrice, Time openTime, Time closeTime, List<Menu> menuList)
    {
        return new StoreResponseDto(userId, name, minOrderPrice, openTime, closeTime, menuList);
    }
}
