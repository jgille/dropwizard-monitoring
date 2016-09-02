package org.jgille.mumon.dropwizard.monitoring.metadata.provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.jgille.mumon.dropwizard.monitoring.metadata.domain.ServiceMetadataProvider;

import java.io.*;
import java.util.Map;

public abstract class YamlServiceMetadataProvider<C> implements ServiceMetadataProvider<C> {

    private final ObjectMapper objectMapper;

    protected YamlServiceMetadataProvider() {
        this(new ObjectMapper(new YAMLFactory()));
    }

    protected YamlServiceMetadataProvider(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static <C > YamlServiceMetadataProvider fromYamlFile(String yamlFileName) {
        return new YamlServiceMetadataProvider<C>() {
            @Override
            protected String getYamlFile(C configuration) {
                return yamlFileName;
            }
        };
    }

    @Override
    public Map<String, Object> metadata(C configuration) {
        File yamlFile = new File(getYamlFile(configuration));
        try (InputStream inputStream = getInputStream(yamlFile)){
            return deserializeYaml(inputStream);
        } catch (IOException e) {
            throw new YamlDeserializationException("Failed to deserialize yaml", e);
        }
    }

    private InputStream getInputStream(File yamlFile) throws FileNotFoundException {
        if (yamlFile.isAbsolute()) {
            return new FileInputStream(yamlFile);
        } else {
            return getClass().getClassLoader().getResourceAsStream(yamlFile.getPath());
        }
    }

    private Map<String, Object> deserializeYaml(InputStream yamlFile) throws IOException {
        return objectMapper.readValue(yamlFile, new TypeReference<Map<String, Object>>() {
        });
    }

    protected abstract String getYamlFile(C configuration);

}
