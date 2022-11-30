package com.example.Meetings_API.Assemblers;

import com.example.Meetings_API.DTOs.MeetingDTO;
import com.example.Meetings_API.DTOs.MeetingResponseDTO;
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

    public static MeetingResponseDTO toResponseDto(Meeting meeting) {
        return new MeetingResponseDTO(meeting.getId(), meeting.getName(), PersonAssembler.toDto(meeting.getResponsiblePerson()), meeting.getDescription(), meeting.getCategory(), meeting.getType(), meeting.getStartDate(), meeting.getEndDate(), meeting.getAttendees());
    }
}
