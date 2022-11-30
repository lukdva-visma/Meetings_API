package com.example.Meetings_API.Assemblers;

import com.example.Meetings_API.DTOs.PersonDTO;
import com.example.Meetings_API.Models.Person;

public class PersonAssembler {
    public static Person mapPerson(PersonDTO personDto) {
        return new Person(personDto.getId(), personDto.getName());
    }

    public static PersonDTO toDto(Person person) {
        return new PersonDTO(person.getId(), person.getName());
    }
}
