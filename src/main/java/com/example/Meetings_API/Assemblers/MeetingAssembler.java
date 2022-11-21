package com.example.Meetings_API.Assemblers;

import com.example.Meetings_API.Models.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
public class MeetingAssembler {
    public Meeting mapMeeting(MeetingDTO meetingDto)
    {
        Meeting meeting  = new Meeting();
        meeting.setName(meetingDto.getName());
        meeting.setResponsiblePerson(meetingDto.getResponsiblePerson());
        meeting.setDescription(meetingDto.getDescription());
        meeting.setCategory(meetingDto.getCategory());
        meeting.setType(meetingDto.getType());
        meeting.setStartDate(meetingDto.getStartDate());
        meeting.setEndDate(meetingDto.getEndDate());
        meeting.addAttendee(meetingDto.getResponsiblePerson());
        return meeting;
    }
}
