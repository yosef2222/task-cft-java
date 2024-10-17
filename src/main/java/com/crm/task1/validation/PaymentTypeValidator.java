package com.crm.task1.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PaymentTypeValidator implements ConstraintValidator<ValidPaymentType, String> {

    private static final String[] VALID_TYPES = {"CASH", "CARD", "TRANSFER"};

    @Override
    public void initialize(ValidPaymentType constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        for (String validType : VALID_TYPES) {
            if (validType.equals(value.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}