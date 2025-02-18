package com.example.personColorAPI.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Configuration class to map color settings from application properties.
 * This class is used to map color settings from the application properties file using the "colors" prefix.
 * The colors are mapped as a map where the key is the person ID (Integer) and the value is the associated color (String).
 */
@Component
@ConfigurationProperties(prefix = "colors")
@Getter
@Setter
public class ColorConfig {
    /**
     * A map of person IDs to their associated colors.
     * This map is populated from the application properties file with the key as the person ID
     * and the value as the color associated with that person.
     */
    private Map<Integer, String> colors;
}
