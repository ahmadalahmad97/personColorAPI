package com.example.personColorAPI;

import com.example.personColorAPI.config.ColorConfig;
import com.example.personColorAPI.model.Person;
import com.example.personColorAPI.service.CsvFileReaderService;
import com.example.personColorAPI.service.CsvImporterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.List;

/**
 * The main entry point of the application.
 * This class contains the main method to run the Spring Boot application and initializes
 * the command line runner to import CSV data during application startup.
 */
@SpringBootApplication
@EnableConfigurationProperties(ColorConfig.class)
@Slf4j
public class PersonColorApiApplication {

    /**
     * Main method to run the Spring Boot application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(PersonColorApiApplication.class, args);
    }

    /**
     * CommandLineRunner bean to load and import data from a CSV file at application startup.
     * It reads the CSV file, processes it, and imports the data into the database.
     *
     * @param csvFileReaderService The service for reading the CSV file.
     * @param csvImporterService   The service for importing persons from the CSV file.
     * @return A CommandLineRunner that runs at application startup.
     */
    @Bean
    public CommandLineRunner loadData(CsvFileReaderService csvFileReaderService, CsvImporterService csvImporterService) {
        return args -> {
            try {
                log.info("Start importing CSV data...");

                List<Person> persons = csvFileReaderService.readCsvFile();

                csvImporterService.importPersons(persons);

                log.info("CSV data successfully imported.");
            } catch (IOException e) {
                log.error("Error importing the CSV data: {}", e.getMessage(), e);
            }
        };
    }
}