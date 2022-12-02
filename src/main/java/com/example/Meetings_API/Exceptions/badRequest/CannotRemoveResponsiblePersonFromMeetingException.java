package com.example.Meetings_API.Exceptions.badRequest;

import com.example.Meetings_API.Exceptions.BaseExceptionInterface;
import com.example.Meetings_API.Models.Meeting;
import com.example.Meetings_API.Models.Person;
import lombok.Getter;

@Getter
public class CannotRemoveResponsiblePersonFromMeetingException extends BadRequestException implements BaseExceptionInterface {

    private final long errorCode = 2L;
    private final Person responsiblePerson;
    private final Meeting meeting;

    public CannotRemoveResponsiblePersonFromMeetingException(Person responsiblePerson, Meeting meeting) {
        super(String.format("ResponsiblePersonId: %s , meetingId: %s", responsiblePerson.getId(), meeting.getId()));
        this.responsiblePerson = responsiblePerson;
        this.meeting = meeting;
    }
}
