package com.team24.outsourcing_project.config.responsewrapper;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private final String status;
    private final T data;

    private ApiResponse(final String status, final T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> ApiResponse<T> ofSuccess(final T data) {
        return new ApiResponse<>("success", data);
    }

    public static <T> ApiResponse<T> ofFail(final T data) {
        return new ApiResponse<>("fail", data);
    }
}
