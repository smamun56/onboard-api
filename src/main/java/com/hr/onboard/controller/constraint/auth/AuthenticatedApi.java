package com.hr.onboard.controller.constraint.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface AuthenticatedApi {
    String rejectMessage() default "";
    int rejectStatus() default 401;
}
