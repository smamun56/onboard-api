package com.hr.onboard.model.auth;

import com.hr.onboard.entity.Base;
import com.hr.onboard.utils.Utils;
import jakarta.persistence.Column;

import java.time.Instant;

public class VerificationCode extends Base {
    @Column(length = 128, nullable = false)
    private String email;

    @Column(length = 5, nullable = false)
    private String code = Utils.randomNumericCode(5);

    @Column(updatable = false, nullable = false)
    private Instant expireAt;
}
