package com.hr.onboard.controller.constraint.checker;

import com.hr.onboard.controller.constraint.rate.LimitTarget;
import com.hr.onboard.controller.constraint.rate.RateLimit;
import com.hr.onboard.exception.ControllerConstraintViolation;
import com.hr.onboard.utils.AuthUtil;
import com.hr.onboard.utils.IPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ControllerRateConstraintChecker {
    public static final Logger logger = LoggerFactory.getLogger(ControllerAuthConstraintChecker.class);
    private static final int MAX_RETRY = 3;

    @Autowired private RedisTemplate<String, Map<String, Object>> redisTemplate;

    public void checkWithMethod(Method method) throws ControllerConstraintViolation {
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);
        if(rateLimit == null) return;
        if (rateLimit.target() == LimitTarget.USER && !AuthUtil.isAuthenticated()) return;

        String targetIdentifier =
                switch (rateLimit.target()){
                    case USER -> AuthUtil.currentUserDetail().getId();
                    default -> IPUtils.getRequestIp();
                };
    }

    public void checkRateLimitByTokenBucket(String key, String targetIdentifier, long limit, long window)
            throws ControllerConstraintViolation {
        final boolean[] isLockFailed = {false};
        final boolean[] isExceed = {false};

        // retry rate limit because we use optimistic lock
        for (int retry = MAX_RETRY; retry > 0; retry--) {
            redisTemplate.execute(
                    new SessionCallback<>() {
                        @Override
                        public List execute(RedisOperations operations) throws DataAccessException {
                            operations.watch(key + "_bucket_for_" + targetIdentifier);
                            Map bucket = operations.opsForHash().entries(key + "_bucket_for_" + targetIdentifier);

                            long currentTime = System.currentTimeMillis();

                            if (bucket == null || bucket.isEmpty()) {
                                bucket = new HashMap<>();
                                bucket.put("token", limit - 1);
                                bucket.put("access_time", currentTime);
                            } else {
                                long tokens = ((Number) bucket.get("token")).longValue();
                                long refill = (long) (
                                        ((currentTime - (long) bucket.get("access_time")) / 1000)
                                                / (window * 1.0 / limit)
                                );
                                tokens = Math.min(tokens + refill, limit);
                                if (tokens > 0) {
                                    bucket.put("token", tokens - 1);
                                    bucket.put("access_time", currentTime);
                                } else {
                                    isExceed[0] = true;
                                }
                            }

                            operations.multi();
                            operations.opsForHash().putAll(key + "_bucket_for_" + targetIdentifier, bucket);
                            operations.expire(
                                    key + "_bucket_for_" + targetIdentifier, Duration.ofSeconds(window));

                            isLockFailed[0] = operations.exec().isEmpty();
                            return null;
                        }
                    }
            );
            // optimistic lock success
            if (!isLockFailed[0]) break;

            // still cannot get/set rate limit in last retry
            if (retry - 1 <= 0)
                throw new RuntimeException("cannot obtain rate limit from redis after retry for " + MAX_RETRY + " times !");
        }

        // exceed rate limit or not
        if (isExceed[0])
            throw new ControllerConstraintViolation(429, "You have sent too many request, please try again later !");
    }
}
