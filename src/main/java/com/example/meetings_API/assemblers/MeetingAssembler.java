package com.example.meetings_API.assemblers;

import com.example.meetings_API.dto.MeetingDTO;
import com.example.meetings_API.dto.MeetingResponseDTO;
import com.example.meetings_API.models.Meeting;

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
        MeetingResponseDTO meetingResponseDTO = new MeetingResponseDTO();
        meetingResponseDTO.setId(meeting.getId());
        meetingResponseDTO.setName(meeting.getName());
        meetingResponseDTO.setResponsiblePerson(PersonAssembler.toDto(meeting.getResponsiblePerson()));
        meetingResponseDTO.setDescription(meeting.getDescription());
        meetingResponseDTO.setCategory(meeting.getCategory());
        meetingResponseDTO.setType(meeting.getType());
        meetingResponseDTO.setStartDate(meeting.getStartDate());
        meetingResponseDTO.setEndDate(meeting.getEndDate());
        meetingResponseDTO.setAttendees(meeting.getAttendees());

        return meetingResponseDTO;
    }
}
