package com.example.personColorAPI.service;

import com.example.personColorAPI.model.Person;
import com.example.personColorAPI.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CsvImporterService {

    private final PersonRepository personRepository;

    public CsvImporterService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void importPersons(List<Person> persons) {
        if (persons == null || persons.isEmpty()) {
            log.warn("No people to import.");
            return;
        }

        List<Person> peopleToSave = new ArrayList<>();
        List<Person> duplicatePeople = new ArrayList<>();

        for (Person person : persons) {
            if (personRepository.existsByNameAndLastnameAndZipcodeAndPersonId(
                    person.getName(), person.getLastname(), person.getZipcode(), person.getPersonId())) {
                duplicatePeople.add(person);
                log.info("The person with the information {} is duplicated and will not be imported.", person);
            } else {
                peopleToSave.add(person);
            }
        }

        if (peopleToSave.isEmpty()) {
            log.info("No new people to import.");
            return;
        }

        try {
            personRepository.saveAll(peopleToSave);
            log.info("{} People imported successfully.", peopleToSave.size());

            if (!duplicatePeople.isEmpty()) {
                log.info("{} duplicate people were skipped.", duplicatePeople.size());
            }

        } catch (Exception e) {
            log.error("Error importing people into the database: {}", e.getMessage(), e);
            throw new RuntimeException("Error importing people into the database.", e);
        }
    }
}