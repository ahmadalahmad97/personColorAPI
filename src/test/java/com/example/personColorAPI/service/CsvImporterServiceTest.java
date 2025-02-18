package com.example.personColorAPI.service;

import com.example.personColorAPI.model.Person;
import com.example.personColorAPI.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CsvImporterService}.
 * This class tests the import functionality of person data using the CSV importer service.
 */
@ExtendWith(MockitoExtension.class)
class CsvImporterServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private CsvImporterService csvImporterService;

    /**
     * Test for importing persons when the list is valid and no duplicates exist.
     * Verifies that the persons are successfully saved to the repository.
     */
    @Test
    public void testImportPersons_Success() {
        List<Person> persons = createPersons();

        when(personRepository.existsByNameAndLastnameAndZipcodeAndPersonId(anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(false);

        when(personRepository.saveAll(anyList())).thenReturn(persons);

        csvImporterService.importPersons(persons);

        verify(personRepository, times(1)).saveAll(eq(persons));
    }

    /**
     * Test for importing persons when there are duplicate entries.
     * Verifies that no persons are saved to the repository if duplicates are detected.
     */
    @Test
    public void testImportPersons_Duplicate() {
        Person person1 = createPerson("John", 1);
        Person person2 = createPerson("John", 1);  // Both persons are duplicates
        List<Person> persons = List.of(person1, person2);

        when(personRepository.existsByNameAndLastnameAndZipcodeAndPersonId(person1.getName(), person1.getLastname(), person1.getZipcode(), person1.getPersonId()))
                .thenReturn(true);

        when(personRepository.existsByNameAndLastnameAndZipcodeAndPersonId(person2.getName(), person2.getLastname(), person2.getZipcode(), person2.getPersonId()))
                .thenReturn(true);

        csvImporterService.importPersons(persons);

        verify(personRepository, times(0)).saveAll(anyList());
    }

    /**
     * Test for importing persons when the input list is empty.
     * Verifies that no save operation is performed when the list is empty.
     */
    @Test
    public void testImportPersons_EmptyList() {
        List<Person> emptyList = new ArrayList<>();

        csvImporterService.importPersons(emptyList);

        verify(personRepository, times(0)).saveAll(anyList());
    }

    /**
     * Test for importing persons when an exception occurs during the save process.
     * Verifies that a RuntimeException is thrown if an error occurs while saving persons.
     */
    @Test
    public void testImportPersons_Failure() {
        List<Person> persons = createPersons();
        String expectedErrorMessage = "Error importing people into the database.";

        when(personRepository.existsByNameAndLastnameAndZipcodeAndPersonId(anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(false);
        when(personRepository.saveAll(anyList())).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            csvImporterService.importPersons(persons);
        });

        verify(personRepository, times(1)).saveAll(persons);

        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    /**
     * Helper method to create a list of mock persons.
     *
     * @return a list of mock Person objects.
     */
    private List<Person> createPersons() {
        Person person1 = createPerson("John", 1);
        Person person2 = createPerson("Jane", 2);
        return List.of(person1, person2);
    }

    /**
     * Helper method to create a mock Person object.
     *
     * @param name     the name of the person
     * @param personId the unique identifier for the person
     * @return a Person object with the given details
     */
    private Person createPerson(String name, int personId) {
        Person person = new Person();
        person.setName(name);
        person.setLastname("Doe");
        person.setPersonId(personId);
        person.setColor("blue");
        person.setCity("San Francisco");
        person.setZipcode("12345");
        return person;
    }
}