package edu.gatech.cs6310;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Map;

public class JsonPropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(
            final String name, final EncodedResource resource) throws IOException {
        Map<String, Object> readValue = new ObjectMapper().readValue(resource.getInputStream(), Map.class);
        return new MapPropertySource("json-property", readValue);
    }

}
