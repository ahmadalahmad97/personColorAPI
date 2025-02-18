package com.example.personColorAPI.repository;

import com.example.personColorAPI.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for handling database operations related to the Person entity.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
    /**
     * Finds persons by their unique personId.
     *
     * @param personId The unique identifier of the person.
     * @return A list of persons matching the given personId.
     */
    List<Person> findByPersonId(int personId);

    /**
     * Finds persons by their color attribute.
     *
     * @param color The color associated with the person.
     * @return A list of persons matching the given color.
     */
    List<Person> findByColor(String color);

    /**
     * Checks if a person with the given name, lastname, zipcode, and personId already exists.
     *
     * @param name     The first name of the person.
     * @param lastname The last name of the person.
     * @param zipcode  The postal code of the person.
     * @param personId The unique identifier of the person.
     * @return true if such a person already exists; false otherwise.
     */
    boolean existsByNameAndLastnameAndZipcodeAndPersonId(String name, String lastname, String zipcode, int personId);
}