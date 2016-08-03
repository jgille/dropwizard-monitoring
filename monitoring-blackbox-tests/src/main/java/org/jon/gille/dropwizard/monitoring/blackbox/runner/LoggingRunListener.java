package org.jon.gille.dropwizard.monitoring.blackbox.runner;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class LoggingRunListener extends RunListener {

    private final Logger logger = LoggerFactory.getLogger("SpockTestRunner");

    @Override
    public void testRunStarted(Description description) {
        logger.info("Started test run");
    }

    @Override
    public void testRunFinished(Result result) {
        Duration runTime = Duration.ofMillis(result.getRunTime());
        logger.info("{} tests run in {} minutes", result.getRunCount(), asMinutes(runTime));
        int failureCount = result.getFailureCount();
        if (failureCount > 0) {
            logger.warn("There were {} failed tests!", failureCount);
        } else {
            logger.info("All tests passed!");
        }
        result.getFailures().forEach(this::logFailureSummary);
    }

    private void logFailureSummary(Failure failure) {
        logger.warn("FAILED: {} - {}", failure.getDescription().getDisplayName(), failure.getMessage());
    }

    @Override
    public void testStarted(Description description) {
        logger.info("Running {}", description.getDisplayName());
    }

    @Override
    public void testFinished(Description description) {
        logger.info("{} finished", description.getDisplayName());
    }

    @Override
    public void testFailure(Failure failure) {
        Description description = failure.getDescription();
        logger.warn("{} FAILED with message {}", description.getDisplayName(), failure.getMessage());
        Throwable exception = failure.getException();
        if (exception != null) {
            logger.warn("Test failed", exception);
        }
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        logger.warn("Test assumption FAILED with message {}", failure.getMessage());
        Throwable exception = failure.getException();
        if (exception != null) {
            logger.warn("Test failed", exception);
        }
    }

    @Override
    public void testIgnored(Description description) {
        logger.info("Ignoring {}", description.getDisplayName());
    }

    private String asMinutes(Duration runTime) {
        long seconds = runTime.getSeconds();
        long s = seconds % 60;
        long m = seconds / 60;
        return String.format("%s:%s", durationFormatted(m), durationFormatted(s));
    }

    private String durationFormatted(long duration) {
        String durationString = String.valueOf(duration);
        return durationString.length() == 1 ? "0" + durationString : durationString;
    }
}
