package com.hr.onboard.filter;

import com.hr.onboard.data.auth.UserDetail;
import com.hr.onboard.exception.InvalidTokenException;
import com.hr.onboard.service.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = null;

        // auth by header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null) accessToken = authHeader.replace("Bearer ", "");

        // auth by cookie
        if (accessToken == null) {
            Cookie cookie = WebUtils.getCookie(request, "access_token");
            if (cookie != null && cookie.getValue() != null) accessToken = cookie.getValue();
        }

        if (accessToken != null) {
            try {
                if (jwtService.isAccessTokenInBlackList(accessToken))
                    throw new InvalidTokenException("invalid token black list !");

                UserDetail userDetail = jwtService.getUserDetailFromAccessToken(accessToken);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
