package com.hr.onboard.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "Spring JWT API by SKR", version = "v1.0.0"))
@SecuritySchemes({
        @SecurityScheme(
                name = "jwt",
                scheme = "bearer",
                bearerFormat = "jwt",
                type = SecuritySchemeType.HTTP,
                in = SecuritySchemeIn.HEADER
        ),
        @SecurityScheme(
                name = "jwt-in-cookie",
                paramName = "access_token",
                scheme = "bearer",
                bearerFormat = "jwt",
                type = SecuritySchemeType.HTTP,
                in = SecuritySchemeIn.COOKIE)
})
@Configuration
public class SpringDocConfig {
}
