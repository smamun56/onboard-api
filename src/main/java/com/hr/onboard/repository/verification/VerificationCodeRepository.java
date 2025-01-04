package com.hr.onboard.repository.verification;

import com.hr.onboard.model.auth.VerificationCode;
import com.hr.onboard.model.auth.VerifyToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.UUID;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, UUID> {
        void deleteByExpireAtLessThan(Instant dateTime);

        long deleteByIdAndEmailAndCodeAndExpireAtGreaterThan(
                UUID id, String email, String code, Instant dateTime);
}
