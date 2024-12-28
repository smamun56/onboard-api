package com.hr.onboard.repository.jwt;

import com.hr.onboard.entity.auth.User;
import com.hr.onboard.model.auth.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccessTokenRepository extends JpaRepository<AccessToken, UUID> {
    Optional<AccessToken> getByIdAndExpireAtGreaterThan(UUID id, Instant dateTime);

    List<AccessToken> getByUser(User user);
}
