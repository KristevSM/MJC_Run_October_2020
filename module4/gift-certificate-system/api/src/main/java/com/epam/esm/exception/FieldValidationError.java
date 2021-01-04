package com.epam.esm.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldValidationError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;
}
