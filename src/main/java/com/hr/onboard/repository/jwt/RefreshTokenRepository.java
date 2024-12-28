package com.hr.onboard.repository.jwt;

import com.hr.onboard.model.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    @Modifying
    @Query("delete from RefreshToken r where r.expireAt < ?1")
    void deleteAllByExpireAtLessThan(Instant dateTime);

    Optional<RefreshToken> getByIdAndExpireAtGreaterThan(UUID id, Instant dateTime);
}
