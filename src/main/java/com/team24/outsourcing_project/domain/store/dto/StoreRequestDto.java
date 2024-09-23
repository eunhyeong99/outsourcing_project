package com.team24.outsourcing_project.domain.store.dto;

import jakarta.validation.constraints.Future;
import lombok.Getter;

import java.sql.Time;
import java.time.LocalTime;

@Getter
public class StoreRequestDto {
    private String name;
    private int minOrderPrice;

    @Future(message = "오픈 시간은 현재 시간보다 미래여야 합니다.")
    private LocalTime openTime;

    @Future(message = "마감 시간은 현재 시간보다 미래여야 합니다.")
    private LocalTime closeTime;

}
