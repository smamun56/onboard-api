package com.hr.onboard.job.request;

import com.hr.onboard.job.handler.CleanUpVerificationsHandler;
import lombok.Data;
import org.jobrunr.jobs.lambdas.JobRequest;

@Data
public class CleanUpVerificationsJob implements JobRequest {
  @Override
  public Class<CleanUpVerificationsHandler> getJobRequestHandler() {
    return CleanUpVerificationsHandler.class;
  }
}
