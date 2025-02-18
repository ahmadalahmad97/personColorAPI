package com.example.personColorAPI.controller;

import com.example.personColorAPI.model.Person;
import com.example.personColorAPI.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class that handles HTTP requests related to persons.
 * This class provides endpoints for retrieving and creating persons.
 */
@RestController
@RequestMapping("/persons")
@Slf4j
public class PersonController {

    private final PersonService personService;

    /**
     * Constructor for the PersonController that injects the PersonService.
     *
     * @param personService The service for managing persons
     */
    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Handles the request to fetch all persons.
     *
     * @return A ResponseEntity containing the list of all persons or a no-content status if the list is empty
     */
    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        log.info("Request received to fetch all persons.");
        List<Person> persons = personService.getAllPersons();
        if (persons.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(persons);
    }

    /**
     * Handles the request to fetch persons by their ID.
     *
     * @param personId The ID of the person to retrieve
     * @return A ResponseEntity containing the list of persons with the specified ID or a not-found status if no persons are found
     */
    @GetMapping("/{personId}")
    public ResponseEntity<List<Person>> getPersonsById(@PathVariable int personId) {
        log.info("Request received to fetch person by ID: {}", personId);
        List<Person> persons = personService.getPersonsById(personId);
        if (persons.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(persons);
    }

    /**
     * Handles the request to fetch persons by their color.
     *
     * @param color The color to search for
     * @return A ResponseEntity containing the list of persons with the specified color or a no-content status if the list is empty
     */
    @GetMapping("/color/{color}")
    public ResponseEntity<List<Person>> getPersonsByColor(@PathVariable String color) {
        log.info("Request received to fetch persons with color: {}", color);
        List<Person> persons = personService.getPersonsByColor(color);
        if (persons.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(persons);
    }

    /**
     * Handles the request to create a new person.
     *
     * @param person The person to be created
     * @return A ResponseEntity containing the created person with a status of 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        log.info("Request received to create a new person: {}", person);
        Person createdPerson = personService.addPerson(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
    }
}


