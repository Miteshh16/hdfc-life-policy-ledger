package com.hdfclife.ledger.validation;

import jakarta.validation.*;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PolicyTypeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
public @interface PolicyType {
    String message() default "type must be TERM, ULIP, or ENDOWMENT";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
