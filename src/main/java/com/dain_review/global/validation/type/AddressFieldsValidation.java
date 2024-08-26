package com.dain_review.global.validation.type;


import com.dain_review.global.validation.AddressFieldsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {AddressFieldsValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AddressFieldsValidation {

    String message() default
            "Address, AddressDetail, and PostalCode must all be provided together or not at all.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
