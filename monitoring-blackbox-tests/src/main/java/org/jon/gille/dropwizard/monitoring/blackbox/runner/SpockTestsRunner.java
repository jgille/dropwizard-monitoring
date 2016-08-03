package org.jon.gille.dropwizard.monitoring.blackbox.runner;

import org.jon.gille.dropwizard.monitoring.blackbox.spec.health.HealthCheckSpec;
import org.jon.gille.dropwizard.monitoring.blackbox.spec.metadata.MetadataSpec;
import org.junit.runner.Computer;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SpockTestsRunner {

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            System.out.println("Loading properties from " + args[0]);
            try (FileInputStream propertiesFile = new FileInputStream(args[0])) {
                Properties properties = System.getProperties();
                properties.load(propertiesFile);
            }
        }
        Computer computer = new Computer();

        JUnitCore jUnitCore = new JUnitCore();
        jUnitCore.addListener(new LoggingRunListener());

        Result result = jUnitCore.run(computer, getSuite());
        System.exit(result.wasSuccessful() ? 0 : 1);
    }

    private static Class<?>[] getSuite() {
        // TODO: We could be using reflection here to find all Specifications

        return new Class[] {
                HealthCheckSpec.class,
                MetadataSpec.class
        };
    }
}
