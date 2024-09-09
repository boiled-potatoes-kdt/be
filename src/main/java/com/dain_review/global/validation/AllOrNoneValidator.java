package com.dain_review.global.validation;


import com.dain_review.global.validation.type.AllOrNone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class AllOrNoneValidator implements ConstraintValidator<AllOrNone, Object> {

    private String[] fields;

    @Override
    public void initialize(AllOrNone constraintAnnotation) {
        this.fields = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        try {
            boolean allNull = true;
            boolean allPresent = true;

            for (String fieldName : fields) {
                Field field = value.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object fieldValue = field.get(value);

                if (fieldValue == null) {
                    allPresent = false;
                } else {
                    allNull = false;
                }
            }

            return allNull || allPresent;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
