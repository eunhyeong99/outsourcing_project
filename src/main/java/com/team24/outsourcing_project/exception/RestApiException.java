package com.team24.outsourcing_project.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestApiException {
    private String errorMessage;
    private int statusCode;
}