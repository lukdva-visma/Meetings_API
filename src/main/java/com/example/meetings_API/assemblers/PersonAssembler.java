package com.example.meetings_API.assemblers;

import com.example.meetings_API.dto.PersonDTO;
import com.example.meetings_API.models.Person;

public class PersonAssembler {
    public static Person mapPerson(PersonDTO personDto) {
        return new Person(personDto.getId(), personDto.getName());
    }

    public static PersonDTO toDto(Person person) {
        return new PersonDTO(person.getId(), person.getName());
    }
}
