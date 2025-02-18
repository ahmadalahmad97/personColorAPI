package com.example.personColorAPI.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a Person entity mapped to a database table.
 * This class is used to model a person with attributes like ID, name, lastname, etc.
 * It is annotated with JPA annotations to map to a database.
 */
@Entity
@Getter
@Setter
public class Person {
    /**
     * The unique identifier for the Person entity.
     * This field is automatically generated as an identity column in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The person's unique identifier, used to match records.
     */
    private int personId;

    /**
     * The first name of the person.
     */
    private String name;

    /**
     * The last name of the person.
     */
    private String lastname;

    /**
     * The postal code associated with the person's address.
     */
    private String zipcode;

    /**
     * The city where the person resides.
     */
    private String city;

    /**
     * The color associated with the person, which can be fetched from a configuration or data.
     */
    private String color;
}