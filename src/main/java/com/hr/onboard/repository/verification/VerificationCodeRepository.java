package com.hr.onboard.repository.verification;

import com.hr.onboard.entity.auth.User;
import com.hr.onboard.model.auth.VerifyToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface VerificationCodeRepository extends JpaRepository<VerifyToken, UUID> {
        void deleteByExpireAtLessThan(Instant dateTime);

        Optional<VerifyToken> getByTokenAndExpireAtGreaterThan(String token, Instant dateTime);

        Optional<VerifyToken> getByUser(User user);

        void deleteByUser(User user);
        }
