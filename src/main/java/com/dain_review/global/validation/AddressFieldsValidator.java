package com.dain_review.global.validation;


import com.dain_review.domain.user.model.request.EnterpriserExtraRegisterRequest;
import com.dain_review.global.validation.type.AddressFieldsValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AddressFieldsValidator
        implements ConstraintValidator<AddressFieldsValidation, EnterpriserExtraRegisterRequest> {

    @Override
    public boolean isValid(
            EnterpriserExtraRegisterRequest value, ConstraintValidatorContext context) {

        // value 가 null 인 경우는 발생하지 않음

        boolean isAddressSet = value.address() != null && !value.address().isBlank();
        boolean isAddressDetailSet =
                value.addressDetail() != null && !value.addressDetail().isBlank();
        boolean isPostalCodeSet = value.postalCode() != null && !value.postalCode().isBlank();

        // 세 필드 중 하나라도 값이 설정된 경우, 모두 설정되어야 함
        return !(isAddressSet || isAddressDetailSet || isPostalCodeSet)
                || (isAddressSet && isAddressDetailSet && isPostalCodeSet);
    }
}
