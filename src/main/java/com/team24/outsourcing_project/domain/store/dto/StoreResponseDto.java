package com.team24.outsourcing_project.domain.store.dto;

import com.team24.outsourcing_project.domain.menu.dto.MenuResponseDto;
import com.team24.outsourcing_project.domain.menu.entity.Menu;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;

@Getter
public class StoreResponseDto {

    private final Long userId;
    private final String name;
    private final int minOrderPrice;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final List<MenuResponseDto> menuList;


    private StoreResponseDto(Long userId, String name, int minOrderPrice, LocalTime openTime,
            LocalTime closeTime, List<Menu> menuList) {
        this.userId = userId;
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.menuList = menuList.stream()
                .map(menu -> new MenuResponseDto(menu.getMenuName(), menu.getMenuPrice()))
                .toList();
    }

    public static StoreResponseDto of(Long userId, String name, int minOrderPrice,
            LocalTime openTime, LocalTime closeTime, List<Menu> menuList) {
        return new StoreResponseDto(userId, name, minOrderPrice, openTime, closeTime, menuList);
    }
}
