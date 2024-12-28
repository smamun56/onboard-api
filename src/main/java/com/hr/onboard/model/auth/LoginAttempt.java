package com.hr.onboard.model.auth;

import com.hr.onboard.config.LoginConfig;
import com.hr.onboard.data.auth.UserDetail;
import com.hr.onboard.exception.InvalidOperation;
import com.hr.onboard.utils.AuthUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.Instant;

@Data
@Embeddable
public class LoginAttempt {
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    @Setter(AccessLevel.PACKAGE)
    int attempts;

    @Column(nullable = true)
    @Setter(AccessLevel.PACKAGE)
    Instant lastAttempt;

    private boolean isExceedLimit(LoginConfig loginConfig){
        return getAttempts() >= loginConfig.getMaxAttempts();
    }

    private boolean canAttempt(LoginConfig loginConfig){
        if(getLastAttempt() != null
        && getLastAttempt().plusSeconds(loginConfig.getCoolTime())
                .isBefore(Instant.now())
        ) {
            return  true;
        }
        return !isExceedLimit(loginConfig);
    }

    private void attempt(LoginConfig loginConfig, boolean success) throws InvalidOperation {
        // check there is to many recent attempts that failure
        if(!canAttempt(loginConfig))
            throw new InvalidOperation("Can not login anymore !");
        if (success){
            // clear when scuucess
            setAttempts(0);
        } else {
            // check exceed limit and reset older failure attempts before increment
            if (isExceedLimit(loginConfig))
                setAttempts(0);
            setAttempts(getAttempts() + 1);
        }
        setLastAttempt(Instant.now());
    }

    public UserDetail login (
            LoginConfig loginConfig,
            AuthenticationManager authenticationManager,
            String username,
            String password
    ) throws InvalidOperation {
        try {
            UserDetail userDetail = AuthUtil.authenticate(authenticationManager, username, password);
            attempt(loginConfig, true);
            return userDetail;
        } catch (BadCredentialsException e) {
            attempt(loginConfig, false);
            throw e;
        }
    }


}
