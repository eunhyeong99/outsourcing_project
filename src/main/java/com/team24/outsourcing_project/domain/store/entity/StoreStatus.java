package com.team24.outsourcing_project.domain.store.entity;

import lombok.Getter;

@Getter
public enum StoreStatus{
    OPEN("영업"),
    OUT("폐업");

    private final String description;

    StoreStatus(String description) {
        this.description = description;
    }

    public String getStoreStatus() {
        return this.description;
    }

}
