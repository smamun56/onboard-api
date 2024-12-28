package com.hr.onboard.repository;

import com.hr.onboard.entity.auth.User;
import com.hr.onboard.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(UUID id);

    Optional<User> getByUserName(String username);

    Optional<User> getByEmail(String email);

    List<User> getByRole(Role role);

    Page<User> findAll(Pageable pageable);
}
