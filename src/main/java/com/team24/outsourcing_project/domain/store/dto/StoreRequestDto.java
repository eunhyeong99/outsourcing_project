package com.team24.outsourcing_project.domain.store.dto;

import jakarta.validation.constraints.Future;
import lombok.Getter;

import java.sql.Time;
import java.time.LocalTime;

@Getter
public class StoreRequestDto {
    private String name;
    private int minOrderPrice;

    private LocalTime openTime;

    private LocalTime closeTime;

}
