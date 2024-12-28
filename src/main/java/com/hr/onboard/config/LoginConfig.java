package com.hr.onboard.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class LoginConfig {
    @Value("${login.maxAttempts}")
    private int maxAttempts;

    @Value("${login.attempts.coolTime}")
    private int coolTime;
}
