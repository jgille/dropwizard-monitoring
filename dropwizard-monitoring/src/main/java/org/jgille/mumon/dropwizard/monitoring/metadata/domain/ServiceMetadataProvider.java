package org.jgille.mumon.dropwizard.monitoring.metadata.domain;

import org.jgille.mumon.dropwizard.monitoring.config.MuMonConfiguration;

import java.util.Map;

public interface ServiceMetadataProvider<C extends MuMonConfiguration> {

    Map<String, Object> metadata(C configuration);
}
