package com.hr.onboard.model.auth;

import com.hr.onboard.entity.Base;
import com.hr.onboard.utils.Utils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

import java.time.Instant;


@Data
@Entity
public class VerificationCode extends Base {
    @Column(length = 128, nullable = false)
    private String email;

    @Column(length = 5, nullable = false)
    private String code = Utils.randomNumericCode(5);

    @Column(updatable = false, nullable = false)
    private Instant expireAt;
}
