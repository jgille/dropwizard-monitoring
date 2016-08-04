package org.jon.gille.dropwizard.monitoring.health.reporting.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.util.Duration;
import io.dropwizard.validation.MaxDuration;
import io.dropwizard.validation.MinDuration;
import org.glassfish.jersey.client.ClientProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.jon.gille.dropwizard.monitoring.health.reporting.BaseHealthReporterFactory;
import org.jon.gille.dropwizard.monitoring.health.reporting.ServiceHealthReporter;

import javax.validation.constraints.NotNull;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.concurrent.TimeUnit;

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
        Client client = ClientBuilder.newClient()
                .property(ClientProperties.CONNECT_TIMEOUT, (int) connectTimeout.toMilliseconds())
                .property(ClientProperties.READ_TIMEOUT, (int) readTimeout.toMilliseconds());

        return new HttpPostHealthReporter(client, uri);
    }
}
