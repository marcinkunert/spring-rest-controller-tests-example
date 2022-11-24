package com.example.springboottesting.errorhandling;

import lombok.Data;

@Data
public class ValidationErrorDto {
    private String field;
    private String message;

    public ValidationErrorDto(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
