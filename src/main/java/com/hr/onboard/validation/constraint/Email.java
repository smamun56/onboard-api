package com.hr.onboard.validation.constraint;

import com.hr.onboard.validation.validator.EmailValidator;
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
@Size(max = 64, message = "email length is at most 64 !")
@NotEmpty(message = "email cannot be empty !")
@Pattern(regexp = EmailValidator.REGEX, message = EmailValidator.NOT_MATCH_MSG)
public @interface Email {
  String message() default EmailValidator.NOT_MATCH_MSG;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
