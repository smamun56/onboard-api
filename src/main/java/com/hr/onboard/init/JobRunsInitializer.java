package com.hr.onboard.init;

import com.hr.onboard.job.request.CleanUpJWTTokensJob;
import com.hr.onboard.job.request.CleanUpVerificationsJob;
import org.jobrunr.scheduling.BackgroundJobRequest;
import org.jobrunr.storage.StorageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;

import java.time.Duration;

public class JobRunsInitializer implements CommandLineRunner {
    @Autowired
    private Environment env;
    @Autowired
    StorageProvider storageProvider;

    @Override
    public void run(String... args) throws Exception {
        createRecurrentJob(env);
    }

    private void createRecurrentJob(Environment env) {
        if (!env.getProperty("init.recurrent-job", Boolean.class, true)) {
            storageProvider
                    .getRecurringJobs()
                    .forEach((recurringJob -> BackgroundJobRequest.delete(recurringJob.getId())));
            return;
        }
        BackgroundJobRequest.scheduleRecurrently(
                "CleanUpJWTTokens", Duration.ofSeconds(1800), new CleanUpJWTTokensJob());
        BackgroundJobRequest.scheduleRecurrently(
                "CleanUpVerifications", Duration.ofSeconds(1800), new CleanUpVerificationsJob());
    }
}
