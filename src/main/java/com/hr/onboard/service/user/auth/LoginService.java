package com.hr.onboard.service.user.auth;

import com.hr.onboard.data.auth.UserDetail;
import org.springframework.security.core.AuthenticationException;

public interface LoginService {
    UserDetail login(String username, String password) throws AuthenticationException;
}
