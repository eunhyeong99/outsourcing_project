package com.team24.outsourcing_project.domain.store.dto;

import java.time.LocalTime;
import lombok.Getter;

@Getter
public class StoreRequestDto {

    private String name;
    private int minOrderPrice;

    private LocalTime openTime;

    private LocalTime closeTime;

}
