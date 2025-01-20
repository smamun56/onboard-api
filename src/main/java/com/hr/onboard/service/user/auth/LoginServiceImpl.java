package com.hr.onboard.service.user.auth;

import com.hr.onboard.config.LoginConfig;
import com.hr.onboard.data.auth.UserDetail;
import com.hr.onboard.entity.auth.User;
import com.hr.onboard.exception.InvalidOperation;
import com.hr.onboard.model.auth.LoginAttempt;
import com.hr.onboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LoginConfig loginConfig;

    @Override
    public UserDetail login(String username, String password) throws AuthenticationException {
        User user = userRepository.getByUserName(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Username is not exist !"));
        LoginAttempt loginAttempt = user.getLoginAttempt();

        try {
            return loginAttempt.login(loginConfig, authenticationManager, username, password);
        } catch (InvalidOperation e) {
            throw new AuthenticationServiceException("You have try too many times, please try again later");
        }
    }
}
