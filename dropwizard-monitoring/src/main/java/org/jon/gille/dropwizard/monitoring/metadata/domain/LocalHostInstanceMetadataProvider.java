package org.jon.gille.dropwizard.monitoring.metadata.domain;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class LocalHostInstanceMetadataProvider implements InstanceMetadataProvider {
    @Override
    public String instanceId() {
        try {
            return Inet4Address.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Unexpected error when getting localhost name", e);
        }
    }
}
