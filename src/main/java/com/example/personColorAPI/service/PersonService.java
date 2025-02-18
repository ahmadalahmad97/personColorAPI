package com.example.personColorAPI.service;

import com.example.personColorAPI.model.Person;
import com.example.personColorAPI.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class that contains the business logic for managing persons.
 * This class provides methods to retrieve and add persons.
 */
@Service
@Slf4j
public class PersonService {

    private final PersonRepository personRepository;

    /**
     * Constructor for the PersonService that injects the PersonRepository.
     *
     * @param personRepository Repository for Person entities
     */
    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Returns a list of all persons from the database.
     *
     * @return A list of persons
     */
    public List<Person> getAllPersons() {
        List<Person> persons = personRepository.findAll();
        log.info("The database contains {} persons.", persons.size());
        return persons;
    }

    /**
     * Returns a list of persons with a specific ID.
     *
     * @param personId The ID of the person to search for
     * @return A list of persons with the specified ID
     */
    public List<Person> getPersonsById(int personId) {
        List<Person> persons = personRepository.findByPersonId(personId);
        if (!persons.isEmpty()) {
            log.info("{} persons with ID {} found.", persons.size(), personId);
        } else {
            log.warn("No persons found with ID {}.", personId);
        }
        return persons;
    }

    /**
     * Returns a list of persons with a specific color.
     *
     * @param color The color to search for
     * @return A list of persons with the specified color
     */
    public List<Person> getPersonsByColor(String color) {
        List<Person> persons = personRepository.findByColor(color);
        log.info("Found {} persons with the color {}.", persons.size(), color);
        return persons;
    }

    /**
     * Adds a new person to the database.
     *
     * @param person The person to be created
     * @return The created person
     */
    public Person addPerson(Person person) {
        log.debug("Attempting to add a new person: {}", person);
        Person createdPerson = personRepository.save(person);
        log.info("Person with ID {} has been added.", createdPerson.getId());
        return createdPerson;
    }
}