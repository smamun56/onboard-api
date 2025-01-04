package com.hr.onboard.entity.auth;

import com.hr.onboard.entity.Base;
import com.hr.onboard.entity.enums.Role;
import com.hr.onboard.model.auth.LoginAttempt;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User extends Base {
    @Version
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT now()")
    private Instant version;

    @Column(unique = true, length = 32, nullable = false)
    private String userName;

    @Column(length = 64, nullable = false)
    private String password;

    @Column(nullable = false, length = 128, unique = true)
    private String email;

    @Column(length = 32, nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.NORMAL;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive = true;

    @CreationTimestamp
    private Instant createAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @Column(nullable = true)
    private Instant authAt;

    @Column(nullable = false)
    private String name;

    @Embedded
    LoginAttempt loginAttempt = new LoginAttempt();

    public User(UUID id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(!(o instanceof User))
            return false;
        User user = (User) o;
        return isActive() == user.isActive()
                && getId().equals(user.getId())
                && getUserName().equals(user.getUserName())
                && getEmail().equals(user.getEmail())
                && getRole().equals(user.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getUserName(),
                getEmail(),
                getName(),
                getRole(),
                isActive()
        );
    }
}
