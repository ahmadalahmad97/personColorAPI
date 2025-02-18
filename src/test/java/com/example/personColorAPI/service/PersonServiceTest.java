package com.example.personColorAPI.service;

import com.example.personColorAPI.model.Person;
import com.example.personColorAPI.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PersonService}.
 * This class tests various methods of the PersonService, which provides operations
 * related to person data such as fetching persons, adding new persons, and handling errors.
 */
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    /**
     * Test for retrieving all persons from the repository.
     * Verifies that the service successfully fetches all persons and returns a non-null result.
     */
    @Test
    public void testGetAllPersons() {
        List<Person> mockPersons = List.of(new Person());
        when(personRepository.findAll()).thenReturn(mockPersons);

        List<Person> result = personService.getAllPersons();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(personRepository, times(1)).findAll();
    }

    /**
     * Test for retrieving persons by their unique ID.
     * Verifies that the service returns the correct persons when queried by ID.
     */
    @Test
    public void testGetPersonsById() {
        List<Person> mockPersons = List.of(new Person());
        when(personRepository.findByPersonId(1)).thenReturn(mockPersons);

        List<Person> result = personService.getPersonsById(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(personRepository, times(1)).findByPersonId(1);
    }

    /**
     * Test for handling cases where no persons are found by their ID.
     * Verifies that the service correctly returns an empty list when no matching persons are found.
     */
    @Test
    public void testGetPersonsById_NotFound() {
        when(personRepository.findByPersonId(1)).thenReturn(List.of());

        List<Person> result = personService.getPersonsById(1);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(personRepository, times(1)).findByPersonId(1);
    }

    /**
     * Test for retrieving persons by their color attribute.
     * Verifies that the service correctly returns a list of persons with the given color.
     */
    @Test
    public void testGetPersonsByColor() {
        List<Person> mockPersons = List.of(new Person());
        when(personRepository.findByColor("blau")).thenReturn(mockPersons);

        List<Person> result = personService.getPersonsByColor("blau");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(personRepository, times(1)).findByColor("blau");
    }

    /**
     * Test for adding a new person to the repository.
     * Verifies that the service successfully saves and returns the person when added.
     */
    @Test
    public void testAddPerson() {
        Person person = createPerson();

        when(personRepository.save(person)).thenReturn(person);

        Person result = personService.addPerson(person);

        assertNotNull(result);
        assertEquals(person, result);
        verify(personRepository, times(1)).save(person);
    }

    /**
     * Test for handling errors during the addition of a new person.
     * Verifies that the service throws a RuntimeException when an error occurs during saving.
     */
    @Test
    public void testAddPerson_Failure() {
        Person person = createPerson();

        when(personRepository.save(person)).thenThrow(new RuntimeException("Error saving person"));

        assertThrows(RuntimeException.class, () -> personService.addPerson(person));
    }

    /**
     * Helper method to create a mock Person object.
     *
     * @return a Person object with pre-set attributes
     */
    private static Person createPerson() {
        Person person = new Person();
        person.setId(1L);
        person.setLastname("Joy");
        person.setName("Rand");
        person.setZipcode("12345");
        person.setCity("Berlin");
        person.setColor("blau");
        person.setPersonId(1);
        return person;
    }
}
