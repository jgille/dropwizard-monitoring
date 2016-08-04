package org.jon.gille.dropwizard.monitoring.health.reporting.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import io.dropwizard.util.Duration;
import io.dropwizard.validation.MaxDuration;
import io.dropwizard.validation.MinDuration;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.logging.LoggingFeature;
import org.hibernate.validator.constraints.NotBlank;
import org.jon.gille.dropwizard.monitoring.health.reporting.BaseHealthReporterFactory;
import org.jon.gille.dropwizard.monitoring.health.reporting.ServiceHealthReporter;

import javax.validation.constraints.NotNull;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@JsonTypeName("http-post")
public class HttpPostHealthReporterFactory extends BaseHealthReporterFactory {

    @NotNull
    @MinDuration(value = 1, unit = TimeUnit.MILLISECONDS)
    @MaxDuration(value = Integer.MAX_VALUE, unit = TimeUnit.MILLISECONDS)
    private Duration connectTimeout = Duration.seconds(10);

    @MinDuration(value = 1, unit = TimeUnit.MILLISECONDS)
    @MaxDuration(value = Integer.MAX_VALUE, unit = TimeUnit.MILLISECONDS)
    private Duration readTimeout = Duration.seconds(5);

    @NotBlank
    private String uri;

    @JsonProperty
    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    @JsonProperty
    public void setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
    }

    @JsonProperty
    public void setUri(String uri) {
        this.uri = uri;
    }

    public HttpPostHealthReporterFactory() {
        super("http-post-health-reporter");
    }

    @Override
    public ServiceHealthReporter build() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(WRITE_DATES_AS_TIMESTAMPS);

        JacksonJaxbJsonProvider jacksonProvider = new JacksonJaxbJsonProvider();
        jacksonProvider.setMapper(objectMapper);

        ClientConfig clientConfig = new ClientConfig(jacksonProvider);

        clientConfig.property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_TEXT);
        clientConfig.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL, Level.FINE.getName());

        Client client = ClientBuilder.newClient(clientConfig)
                .property(ClientProperties.CONNECT_TIMEOUT, (int) connectTimeout.toMilliseconds())
                .property(ClientProperties.READ_TIMEOUT, (int) readTimeout.toMilliseconds());

        return new HttpPostHealthReporter(client, uri);
    }
}
