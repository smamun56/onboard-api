package com.hr.onboard.validation.validator;

import com.hr.onboard.exception.ValidationError;
import com.hr.onboard.validation.constraint.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleValidator
        implements ConstraintValidator<Role, String>,
        Validator<com.hr.onboard.entity.enums.Role, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            com.hr.onboard.entity.enums.Role.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public com.hr.onboard.entity.enums.Role validate(String data) throws ValidationError {
        try {
            return com.hr.onboard.entity.enums.Role.valueOf(data);
        } catch (IllegalArgumentException e) {
            throw new ValidationError("role " + data + " is not exist !");
        }
    }

    private static final RoleValidator instance = new RoleValidator();

    public static RoleValidator getInstance() {
        return instance;
    }

    private RoleValidator() {}

}
