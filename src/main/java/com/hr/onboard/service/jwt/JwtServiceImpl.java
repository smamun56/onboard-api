package com.hr.onboard.service.jwt;

import com.hr.onboard.config.JwtConfig;
import com.hr.onboard.data.auth.AccessTokenSpec;
import com.hr.onboard.data.auth.TokenPair;
import com.hr.onboard.data.auth.UserDetail;
import com.hr.onboard.entity.auth.User;
import com.hr.onboard.exception.InvalidOperation;
import com.hr.onboard.exception.InvalidTokenException;
import com.hr.onboard.exception.UserDoesNotExist;
import com.hr.onboard.model.auth.AccessToken;
import com.hr.onboard.model.auth.RefreshToken;
import com.hr.onboard.repository.UserRepository;
import com.hr.onboard.repository.jwt.AccessTokenRepository;
import com.hr.onboard.repository.jwt.RefreshTokenRepository;
import com.hr.onboard.service.redis.RedisService;
import com.hr.onboard.utils.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    UserRepository userRepository;
    @Autowired private AccessTokenRepository accessTokenRepository;
    @Autowired private RefreshTokenRepository refreshTokenRepository;
    @Autowired private RedisService redisService;
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private AccessToken generateAccessToken(User user){
        return new AccessToken(jwtConfig, user);
    }

    private RefreshToken generateRefreshToken(AccessToken accessToken){
        return new RefreshToken(jwtConfig, accessToken);
    }

    private TokenPair createTokens(User user) {
        AccessToken accessToken = new AccessToken(jwtConfig, user);
        RefreshToken refreshToken = new RefreshToken(jwtConfig, accessToken);
        refreshTokenRepository.save(refreshToken);
        return new TokenPair(accessToken, accessToken.getRefreshToken());
    }

    public TokenPair issueTokens(UserDetail userDetail) throws UserDoesNotExist {
        User user = userRepository.getByUserName(userDetail.getUsername())
                .orElseThrow(()-> new UserDoesNotExist("user is not exist !"));
        TokenPair tokenPair = createTokens(user);

        // prevent concurrent user role/password/active change
        user.setAuthAt(Instant.now());
        return tokenPair;
    }

    @Override
    public TokenPair refreshTokens(String refreshPlainToken) throws InvalidTokenException, InvalidOperation {
        // parse refresh token
        String jti;
        try {
            jti = JwtUtil.parseRefreshToken(jwtConfig.getPublicKey(), refreshPlainToken).getJti();
        } catch (JwtException e){
            throw new InvalidTokenException("Invalid refresh token !");
        }
        // load refresh token
        RefreshToken refreshToken = refreshTokenRepository
                .getByIdAndExpireAtGreaterThan(UUID.fromString(jti), Instant.now())
                .orElseThrow(() -> new InvalidTokenException("invalid refresh token !"));
        User user = refreshToken.getUser();
        if(!user.isActive()) throw new InvalidOperation("cannot refresh tokens for inactive user !");

        // refresh token will be cascaded deleted
        revokeAccessToken(refreshToken.getAccessToken());

        // issue new tokens
        AccessToken accessToken = generateAccessToken(user);
        RefreshToken newRefreshToken = generateRefreshToken(accessToken);

        // prevent concurrent user role/password/active change
        user.setAuthAt(Instant.now());
        userRepository.save(user);

        return new TokenPair(accessToken, newRefreshToken);
    }

    @Override
    public UserDetail getUserDetailFromAccessToken(String token) throws InvalidTokenException {
        return JwtUtil.extractUserDetailFromAccessToken(jwtConfig.getPublicKey(), token);
    }

    @Override
    public AccessTokenSpec introspect(String token) throws InvalidTokenException {
        if (isAccessTokenInBlackList(token)) throw new InvalidTokenException("invalid token !");
        return JwtUtil.parseAccessToken(jwtConfig.getPublicKey(), token);
    }

    @Override
    public void revokeAccessToken(String id) {
        AccessToken accessToken =
                accessTokenRepository
                        .getByIdAndExpireAtGreaterThan(UUID.fromString(id), Instant.now())
                        .orElse(null);
        if (accessToken == null) return;
        accessTokenRepository.delete(accessToken); // refreshToken will be cascade deleted
        addAccessTokenToBlackList(accessToken);
    }

    @Override
    public void revokeAccessToken(AccessToken accessToken) {
        accessTokenRepository.delete(accessToken); // refreshToken will be cascade deleted
        addAccessTokenToBlackList(accessToken);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void revokeAccessToken(List<AccessToken> accessTokens) {
        accessTokenRepository.deleteAll(accessTokens); // refreshToken will be cascade deleted
        accessTokens.forEach(this::addAccessTokenToBlackList);
    }

    @Override
    public void addAccessTokenToBlackList(AccessToken accessToken) {
        redisService.set(
                "revoked_access_token:{" + accessToken.getToken() + "}",
                "",
                Duration.ofSeconds(jwtConfig.getAccessTokenLifetimeSec()));
    }

    @Override
    public boolean isAccessTokenInBlackList(String accessPlainToken) {
        return redisService.has("revoked_access_token:{" + accessPlainToken + "}");
    }

    @Transactional
    @Override
    public void deleteExpiredTokens() {
        logger.info("delete expired tokens");
        // access token will be cascade delete
        refreshTokenRepository.deleteAllByExpireAtLessThan(Instant.now());
    }




}
