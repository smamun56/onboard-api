package com.hr.onboard.validation.constraint;

import com.hr.onboard.validation.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Constraint(validatedBy = {})
@Retention(RUNTIME)
@Size(min = 8, message = "password length is at least 8 !")
@Size(max = 32, message = "password length is at most 32 !")
@NotEmpty(message = "password cannot be empty !")
@Pattern(regexp = PasswordValidator.REGEX, message = PasswordValidator.NOT_MATCH_MSG)
public @interface Password {
  String message() default PasswordValidator.NOT_MATCH_MSG;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
