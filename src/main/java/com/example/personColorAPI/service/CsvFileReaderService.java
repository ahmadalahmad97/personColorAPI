package com.example.personColorAPI.service;

import com.example.personColorAPI.config.ColorConfig;
import com.example.personColorAPI.config.FileConfig;
import com.example.personColorAPI.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for reading CSV files and converting the data into Person objects.
 * This service is responsible for reading the file, processing the rows, and creating Person instances.
 */
@Service
@Slf4j
public class CsvFileReaderService {

    private final FileConfig fileConfig;
    private final ColorConfig colorConfig;

    /**
     * Constructor for the CsvFileReaderService that injects the FileConfig and ColorConfig.
     *
     * @param fileConfig Configuration for file path settings
     * @param colorConfig Configuration for color mappings for persons
     */
    public CsvFileReaderService(FileConfig fileConfig, ColorConfig colorConfig) {
        this.fileConfig = fileConfig;
        this.colorConfig = colorConfig;
    }

    /**
     * Reads the CSV file, processes each row, and converts it into a list of Person objects.
     *
     * @return A list of Person objects populated from the CSV file
     * @throws IOException If an error occurs while reading the file
     */
    public List<Person> readCsvFile() throws IOException {
        List<Person> persons = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileConfig.getFilePath()))) {
            String csvRow;

            while ((csvRow = br.readLine()) != null) {
                if (!csvRow.trim().isEmpty()) {
                    String[] fields = csvRow.split(",");
                    if (fields.length == 4) {
                        createPersonFromCsvFields(fields, persons);
                    } else {
                        log.warn("The row does not have the expected length: {}", csvRow);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error occurred while reading the CSV file at path: {}. Exception: {}", fileConfig.getFilePath(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("An unexpected error occurred while processing the CSV file: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing the CSV file", e);
        }
        return persons;
    }

    /**
     * Creates a Person object from the CSV row fields and adds it to the list of persons.
     *
     * @param fields  The array of CSV fields for a single row
     * @param persons The list to which the created Person will be added
     */
    private void createPersonFromCsvFields(String[] fields, List<Person> persons) {
        try {
            String personIdString = fields[3].trim();
            int personId = Integer.parseInt(personIdString);
            String color = getColorForPerson(personId);
            String[] plzAndCity = extractZipAndCity(fields[2]);
            Person person = createPerson(fields, plzAndCity, color, personId);
            persons.add(person);
        } catch (NumberFormatException e) {
            log.error("Error processing the ID: {} - {}", fields[3], e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while mapping person: {}. Error: {}", String.join(",", fields), e.getMessage(), e);
        }
    }

    /**
     * Retrieves the color associated with the person ID from the color configuration.
     *
     * @param personId The ID of the person for which to get the color
     * @return The color associated with the person, or "UNKNOWN" if no color is found
     */
    private String getColorForPerson(int personId) {
        if (colorConfig == null || colorConfig.getColors() == null) {
            log.error("ColorConfig or Colors map is null, returning default color 'UNKNOWN'.");
            return "UNKNOWN";
        }
        return colorConfig.getColors().getOrDefault(personId, "UNKNOWN");
    }

    /**
     * Creates a Person object from the provided CSV fields and associated data.
     *
     * @param fields     The CSV fields containing the person's data
     * @param plzAndCity The extracted zipcode and city for the person
     * @param color      The color associated with the person
     * @param personId   The person ID
     * @return A new Person object populated with the provided data
     */
    private Person createPerson(String[] fields, String[] plzAndCity, String color, int personId) {
        Person person = new Person();
        person.setLastname(fields[0].trim());
        person.setName(fields[1].trim());
        person.setZipcode(plzAndCity[0]);
        person.setCity(plzAndCity[1]);
        person.setColor(color);
        person.setPersonId(personId);
        return person;
    }

    /**
     * Extracts the zipcode and city from a given string in the format "ZIPCODE CITY".
     * The method will clean and split the string accordingly.
     *
     * @param zipAndCity The string containing the zipcode and city
     * @return An array containing the zipcode at index 0 and the city at index 1
     */
    private String[] extractZipAndCity(String zipAndCity) {
        String cleanedZipAndCity = zipAndCity.replaceAll("[^a-zA-Z0-9\\säöüßÄÖÜ ]", "").trim();
        String[] zipAndCityParts = cleanedZipAndCity.split(" ", 2);

        if (cleanedZipAndCity.isEmpty()) {
            log.warn("Empty zip and city data found: {}", zipAndCity);
            return new String[]{"", ""};
        }
        if (zipAndCityParts.length < 2) {
            log.warn("Failed to split zip and city correctly: {}", zipAndCity);
            return new String[]{zipAndCityParts[0], ""};
        }

        return zipAndCityParts;
    }
}