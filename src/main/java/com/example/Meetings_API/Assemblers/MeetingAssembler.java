package com.example.Meetings_API.Assemblers;

import com.example.Meetings_API.DTOs.MeetingDTO;
import com.example.Meetings_API.Models.Meeting;

public class MeetingAssembler {
    public static Meeting mapMeeting(MeetingDTO meetingDto) {
        Meeting meeting = new Meeting();
        meeting.setName(meetingDto.getName());
        meeting.setResponsiblePerson(PersonAssembler.mapPerson(meetingDto.getResponsiblePerson()));
        meeting.setDescription(meetingDto.getDescription());
        meeting.setCategory(meetingDto.getCategory());
        meeting.setType(meetingDto.getType());
        meeting.setStartDate(meetingDto.getStartDate());
        meeting.setEndDate(meetingDto.getEndDate());
        meeting.addAttendee(PersonAssembler.mapPerson(meetingDto.getResponsiblePerson()));
        return meeting;
    }
}
