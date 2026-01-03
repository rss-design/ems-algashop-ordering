package com.algaworks.algashop.ordering.domain.model.commons;

import com.algaworks.algashop.ordering.domain.model.FieldValidations;
import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.VALIDATION_ERROR_INVALID_EMAIL;

public record Email(String value) {

    public Email {
        FieldValidations.requiresValidEmail(value, VALIDATION_ERROR_INVALID_EMAIL);
    }

    @Override
    public String toString() {
        return value;
    }
}
