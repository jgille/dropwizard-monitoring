package org.jon.gille.dropwizard.monitoring.sample.monitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.logging.LoggingFeature;

import java.util.logging.Level;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class SampleMonitorApp extends Application<Configuration> {
    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        ObjectMapper objectMapper = environment.getObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(WRITE_DATES_AS_TIMESTAMPS);

        JerseyEnvironment jersey = environment.jersey();
        jersey.property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_TEXT);
        jersey.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL, Level.INFO.getName());
        jersey.register(new InMemoryMonitorResource());
    }

    public static void main(String[] args) throws Exception {
        new SampleMonitorApp().run(args);
    }
}
