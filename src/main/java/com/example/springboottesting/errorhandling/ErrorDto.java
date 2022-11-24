package com.example.springboottesting.errorhandling;

import lombok.Data;

import java.util.List;

@Data
public class ErrorDto {

    private int code;
    private String message;
    private List<ValidationErrorDto> validationErrors;

    public ErrorDto(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorDto(int code, String message, List<ValidationErrorDto> validationErrors) {
        this.code = code;
        this.message = message;
        this.validationErrors = validationErrors;
    }
}
