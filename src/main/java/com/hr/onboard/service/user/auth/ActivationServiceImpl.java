package com.hr.onboard.service.user.auth;

import com.hr.onboard.entity.auth.User;
import com.hr.onboard.entity.enums.Role;
import com.hr.onboard.exception.InvalidOperation;
import com.hr.onboard.exception.UserDoesNotExist;
import com.hr.onboard.repository.UserRepository;
import com.hr.onboard.repository.jwt.AccessTokenRepository;
import com.hr.onboard.service.jwt.JwtService;
import com.hr.onboard.utils.AuthUtil;
import com.hr.onboard.validation.validator.UUIDValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ActivationServiceImpl implements ActivationService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;
    @Autowired
    AccessTokenRepository accessTokenRepository;
    UUIDValidator uuidValidator = UUIDValidator.getInstance();

    @Retryable(value = OptimisticLockingFailureException.class, backoff = @Backoff(delay = 100))
    @Override
    public void activateUser(String userId) throws InvalidOperation, UserDoesNotExist {
        UUID id = uuidValidator.validate(userId);

        User user =
                userRepository.findById(id).orElseThrow(() -> new UserDoesNotExist("user is not exist !"));
        if (AuthUtil.isAuthenticated() && AuthUtil.currentUserDetail().getId().equals(id.toString()))
            throw new InvalidOperation("cannot activate yourself !");
        if (user.isActive()) throw new InvalidOperation("target user is already active !");

        user.setActive(true);
        userRepository.save(user);
    }

    @Retryable(value = OptimisticLockingFailureException.class, backoff = @Backoff(delay = 100))
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deactivateUser(String userId) throws InvalidOperation, UserDoesNotExist {
        UUID id = uuidValidator.validate(userId);

        User user =
                userRepository.findById(id).orElseThrow(() -> new UserDoesNotExist("user is not exist !"));
        if (AuthUtil.isAuthenticated() && AuthUtil.currentUserDetail().getId().equals(id.toString()))
            throw new InvalidOperation("cannot deactivate yourself !");
        if (user.getRole() == Role.ADMIN) throw new InvalidOperation("cannot deactivate an admin !");
        if (!user.isActive()) throw new InvalidOperation("target user is already inactive !");

        user.setActive(false);
        userRepository.save(user);

        // need to logout user after deactivate
        jwtService.revokeAccessToken(accessTokenRepository.getByUser(user));
    }
}
