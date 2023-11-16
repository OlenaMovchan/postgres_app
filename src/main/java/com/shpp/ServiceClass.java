package com.shpp;

import jakarta.validation.*;
import java.util.Set;

public class ServiceClass {

    private final Validator validator;

    public ServiceClass() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public  <T> boolean validateDTO(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ValidationException("DTO validation failed");
        } else {
           return true;
        }
    }
}
