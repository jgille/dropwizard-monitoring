package org.jgille.mumon.dropwizard.monitoring.metadata.domain;

import java.util.Map;

public interface ServiceMetadataProvider<C> {

    Map<String, Object> metadata(C configuration);
}
