package com.hr.onboard.data.auth;

import com.hr.onboard.model.auth.AccessToken;
import com.hr.onboard.model.auth.RefreshToken;
import lombok.Data;

@Data
public class TokenPair {
    private AccessToken accessToken;
    private RefreshToken refreshToken;

    public TokenPair(AccessToken accessToken, RefreshToken refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
