package com.hdfclife.ledger.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PolicyTypeValidator implements ConstraintValidator<PolicyType, String> {
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && (value.equals("TERM") || value.equals("ULIP") || value.equals("ENDOWMENT"));
    }
}
