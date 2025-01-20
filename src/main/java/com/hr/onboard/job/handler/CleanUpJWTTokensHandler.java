package com.hr.onboard.job.handler;

import com.hr.onboard.job.request.CleanUpJWTTokensJob;
import com.hr.onboard.service.jwt.JwtService;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.lambdas.JobRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;

public class CleanUpJWTTokensHandler implements JobRequestHandler<CleanUpJWTTokensJob> {


    @Autowired
    private JwtService jwtService;

    @Job(name = "delete all expired refresh tokens and related access tokens")
    @Override
    public void run(CleanUpJWTTokensJob job) throws Exception {
        jwtService.deleteExpiredTokens();
    }
}
