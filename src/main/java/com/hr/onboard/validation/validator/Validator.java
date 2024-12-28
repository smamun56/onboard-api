package com.hr.onboard.validation.validator;

import com.hr.onboard.exception.ValidationError;

public interface Validator <O, I> {

    O validate(I data) throws ValidationError;
}
