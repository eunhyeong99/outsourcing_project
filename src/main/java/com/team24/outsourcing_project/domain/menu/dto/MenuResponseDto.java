package com.team24.outsourcing_project.domain.menu.dto;

import lombok.Getter;

@Getter
public class MenuResponseDto {
    private String menuName;
    private int menuPrice;

    public MenuResponseDto(String menuName, int menuPrice) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }
}
