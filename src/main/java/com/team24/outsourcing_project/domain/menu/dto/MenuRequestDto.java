package com.team24.outsourcing_project.domain.menu.dto;

import com.team24.outsourcing_project.domain.store.entity.Store;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuRequestDto {

    private Long storeId;
    private String menuName;
    private int menuPrice;

}
