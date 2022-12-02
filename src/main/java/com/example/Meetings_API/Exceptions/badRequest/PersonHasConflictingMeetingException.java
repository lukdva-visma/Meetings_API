package com.example.Meetings_API.Exceptions.badRequest;

import com.example.Meetings_API.Exceptions.BaseExceptionInterface;
import com.example.Meetings_API.Models.Meeting;
import com.example.Meetings_API.Models.Person;
import lombok.Getter;

@Getter
public class PersonHasConflictingMeetingException extends BadRequestException implements BaseExceptionInterface {
    private final Person person;
    private final Meeting meeting;
    private final long errorCode = 1L;


    public PersonHasConflictingMeetingException(Person person, Meeting meeting) {
        super(String.format("PersonId: %s , Meeting to be added to Id: %s", person.getId(), meeting.getId()));
        this.person = person;
        this.meeting = meeting;
    }

}
