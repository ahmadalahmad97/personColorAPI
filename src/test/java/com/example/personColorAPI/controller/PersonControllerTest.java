package com.example.personColorAPI.controller;

import com.example.personColorAPI.model.Person;
import com.example.personColorAPI.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link PersonController}.
 * This class verifies the controller methods that interact with the {@link PersonService}.
 */
@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    /**
     * Test for {@link PersonController#getAllPersons()}.
     * Verifies if the controller returns all persons when available.
     */
    @Test
    public void testGetAllPersons() {
        List<Person> mockPersons = createPersons();
        when(personService.getAllPersons()).thenReturn(mockPersons);

        ResponseEntity<List<Person>> response = personController.getAllPersons();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Test for {@link PersonController#getAllPersons()} when no persons are available.
     * Verifies if the controller returns a NO_CONTENT status when the person list is empty.
     */
    @Test
    public void testGetAllPersons_NoContent() {
        when(personService.getAllPersons()).thenReturn(new ArrayList<>());

        ResponseEntity<List<Person>> response = personController.getAllPersons();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Test for {@link PersonController#getPersonsById(int)} when persons are found.
     * Verifies if the controller returns persons based on their ID.
     */
    @Test
    public void testGetPersonsById_Found() {
        List<Person> persons = createPersons();
        when(personService.getPersonsById(1)).thenReturn(persons);

        ResponseEntity<List<Person>> response = personController.getPersonsById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Test for {@link PersonController#getPersonsById(int)} when no persons are found.
     * Verifies if the controller returns a NOT_FOUND status when no persons match the ID.
     */
    @Test
    public void testGetPersonsById_NotFound() {
        when(personService.getPersonsById(1)).thenReturn(new ArrayList<>());

        ResponseEntity<List<Person>> response = personController.getPersonsById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Test for {@link PersonController#getPersonsByColor(String)}.
     * Verifies if the controller returns persons by color.
     */
    @Test
    public void testGetPersonsByColor() {
        List<Person> mockPersons = createPersons();
        when(personService.getPersonsByColor("blau")).thenReturn(mockPersons);

        ResponseEntity<List<Person>> response = personController.getPersonsByColor("blau");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Test for {@link PersonController#getPersonsByColor(String)} when no persons are found.
     * Verifies if the controller returns a NO_CONTENT status when no persons match the color.
     */
    @Test
    public void testGetPersonsByColor_NoContent() {
        when(personService.getPersonsByColor("grün")).thenReturn(new ArrayList<>());

        ResponseEntity<List<Person>> response = personController.getPersonsByColor("grün");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Test for {@link PersonController#createPerson(Person)}.
     * Verifies if the controller correctly creates a new person.
     */
    @Test
    public void testCreatePerson() {
        Person person = new Person();
        person.setName("John");
        person.setLastname("Doe");

        when(personService.addPerson(person)).thenReturn(person);

        ResponseEntity<Person> response = personController.createPerson(person);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Helper method to create a list of mock persons.
     *
     * @return a list of mock Person objects.
     */
    private List<Person> createPersons() {
        Person person1 = new Person();
        person1.setId(1L);
        person1.setLastname("Joy");
        person1.setName("Rand");
        person1.setZipcode("12345");
        person1.setCity("Berlin");
        person1.setColor("blau");
        person1.setPersonId(1);

        Person person2 = new Person();
        person2.setId(2L);
        person2.setLastname("Roy");
        person2.setName("Say");
        person2.setZipcode("4321");
        person2.setCity("Leipzig");
        person2.setColor("red");
        person2.setPersonId(1);

        return List.of(person1, person2);
    }
}