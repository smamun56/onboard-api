package com.hr.onboard.job.request;

import com.hr.onboard.job.handler.CleanUpJWTTokensHandler;
import lombok.Data;
import org.jobrunr.jobs.lambdas.JobRequest;
import org.jobrunr.jobs.lambdas.JobRequestHandler;

@Data
public class CleanUpJWTTokensJob implements JobRequest {

    @Override
    public Class<CleanUpJWTTokensHandler> getJobRequestHandler() {
        return CleanUpJWTTokensHandler.class;
    }
}
