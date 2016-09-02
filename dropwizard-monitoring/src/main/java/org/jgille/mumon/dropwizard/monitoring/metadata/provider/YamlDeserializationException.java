package org.jgille.mumon.dropwizard.monitoring.metadata.provider;

public class YamlDeserializationException extends RuntimeException {
    public YamlDeserializationException(String message, Exception cause) {
        super(message, cause);
    }
}
