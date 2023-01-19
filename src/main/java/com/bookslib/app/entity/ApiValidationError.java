package com.bookslib.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public
class ApiValidationError {
    private String fieldName;
    private Object invalidValue;
    private String errorMessage;

    public ApiValidationError(String fieldName, Object invalidValue) {
        this.fieldName = fieldName;
        this.invalidValue = invalidValue;
    }
}
