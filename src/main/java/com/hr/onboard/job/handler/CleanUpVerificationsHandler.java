package com.hr.onboard.job.handler;

import com.hr.onboard.job.request.CleanUpVerificationsJob;
import com.hr.onboard.service.verification.VerificationService;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.lambdas.JobRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CleanUpVerificationsHandler implements JobRequestHandler<CleanUpVerificationsJob> {
  @Autowired
  VerificationService verificationService;

  @Job(name = "delete all expired verification codes and tokens")
  @Override
  public void run(CleanUpVerificationsJob job) throws Exception {
    verificationService.deleteExpiredVerificationCodes();
    verificationService.deleteExpiredVerifyTokens();
  }
}
