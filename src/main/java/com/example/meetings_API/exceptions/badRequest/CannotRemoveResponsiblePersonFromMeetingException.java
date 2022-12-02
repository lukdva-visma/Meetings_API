package com.example.meetings_API.exceptions.badRequest;

import com.example.meetings_API.exceptions.BaseExceptionInterface;
import com.example.meetings_API.models.Meeting;
import com.example.meetings_API.models.Person;
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
