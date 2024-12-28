package com.hr.onboard.controller.constraint.auth;

import com.hr.onboard.entity.enums.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.METHOD})
@Retention(RUNTIME)
@AuthenticatedApi
public @interface ApiRejectTo {
    Role[] roles() default {};

    String rejectMessage() default "You don't have enough permission !";

    int rejectStatus() default 403;
}
