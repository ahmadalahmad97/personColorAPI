package com.example.personColorAPI.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration class to map file-related settings from the application properties.
 * This class maps the file path configuration for reading files.
 * The file path is retrieved from the application's properties using the "file" prefix.
 */
@Component
@ConfigurationProperties(prefix = "file")
@Getter
@Setter
public class FileConfig {
    /**
     * The file path used to locate the input file for processing.
     * This value is populated from the application properties file under the "file" prefix.
     */
    private String filePath;
}
