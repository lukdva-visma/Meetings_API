package com.example.meetings_API.exceptions.badRequest;

import com.example.meetings_API.exceptions.BaseExceptionInterface;
import com.example.meetings_API.models.Meeting;
import com.example.meetings_API.models.Person;
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
