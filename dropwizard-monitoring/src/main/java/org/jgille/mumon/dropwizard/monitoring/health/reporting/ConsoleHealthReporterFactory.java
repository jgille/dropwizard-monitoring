package org.jgille.mumon.dropwizard.monitoring.health.reporting;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("console")
public class ConsoleHealthReporterFactory extends BaseHealthReporterFactory {

    public ConsoleHealthReporterFactory() {
        super("console-health-reporter");
    }

    @Override
    public ServiceHealthReporter build() {
        return new ConsoleHealthReporter();
    }

}

