package com.hr.onboard.model.auth;

import com.hr.onboard.entity.Base;
import com.hr.onboard.entity.auth.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Data
@Entity
public class VerifyToken extends Base {
    @Column(unique = true, updatable = false, nullable = false, columnDefinition = "TEXT")
    private String token;

    @Column(updatable = false, nullable = false)
    private Instant expireAt;

    @OneToOne
    @JoinColumn(unique = true) // unidirectional one to one
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
