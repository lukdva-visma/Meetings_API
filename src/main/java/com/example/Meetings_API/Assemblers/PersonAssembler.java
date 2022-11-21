package com.example.Meetings_API.Assemblers;

import com.example.Meetings_API.Models.Person;
import com.example.Meetings_API.Models.PersonDTO;
import org.springframework.stereotype.Component;

@Component
public class PersonAssembler {
    public Person mapPerson(PersonDTO personDto) {
        Person person = new Person(personDto.getId(), personDto.getName());
        return person;
    }
}
