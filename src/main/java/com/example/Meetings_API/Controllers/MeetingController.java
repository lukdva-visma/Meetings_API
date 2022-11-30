package com.example.Meetings_API.Controllers;

import com.example.Meetings_API.Assemblers.MeetingAssembler;
import com.example.Meetings_API.Assemblers.PersonAssembler;
import com.example.Meetings_API.Configurations.ConfigProperties;
import com.example.Meetings_API.DTOs.MeetingDTO;
import com.example.Meetings_API.DTOs.MeetingResponseDTO;
import com.example.Meetings_API.DTOs.PersonDTO;
import com.example.Meetings_API.Models.Meeting;
import com.example.Meetings_API.Models.MeetingFilter;
import com.example.Meetings_API.Models.Person;
import com.example.Meetings_API.Services.MeetingsService;
import com.example.Meetings_API.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MeetingController {

    MeetingsService meetingsService;

    JwtUtils jwtUtils;

    ConfigProperties config;

    @Autowired
    public MeetingController(MeetingsService meetingService, JwtUtils jwtUtils, ConfigProperties config) {
        this.meetingsService = meetingService;
        this.jwtUtils = jwtUtils;
        this.config = config;
    }

    @PostMapping("/meetings")
    public MeetingResponseDTO createMeeting(@RequestBody MeetingDTO meetingDto) {
        Meeting meeting = MeetingAssembler.mapMeeting(meetingDto);
        meetingsService.addMeeting(meeting);
        MeetingResponseDTO meetingResponseDTO = MeetingAssembler.toResponseDto(meeting);
        return meetingResponseDTO;
    }

    @DeleteMapping("/meetings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMeeting(@RequestHeader(name = "Authorization") String token, @PathVariable String id) {
        String personId = jwtUtils.getPersonIdFromToken(token);
        meetingsService.deleteMeeting(id, personId);
    }

    @PostMapping("/meetings/{id}/attendees")
    public MeetingResponseDTO addAttendee(@PathVariable String id, @RequestBody PersonDTO personDto) {
        Person person = PersonAssembler.mapPerson(personDto);
        Meeting updatedMeeting = meetingsService.addPersonToMeeting(id, person);
        MeetingResponseDTO meetingResponseDTO = MeetingAssembler.toResponseDto(updatedMeeting);
        return meetingResponseDTO;
    }

    @DeleteMapping("/meetings/{meetingId}/attendees/{attendeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAttendee(@PathVariable String meetingId, @PathVariable String attendeeId) {
        Meeting updatedMeeting = meetingsService.removeAttendeeFromMeeting(meetingId, attendeeId);
    }

    @GetMapping("/meetings")
    public List<MeetingResponseDTO> getMeetings(MeetingFilter filters) {
        List<Meeting> meetings = meetingsService.getFilteredMeetings(filters);
        List<MeetingResponseDTO> responseMeetings = meetings.stream().map(meeting -> MeetingAssembler.toResponseDto(meeting)).collect(Collectors.toList());
        return responseMeetings;
    }
}
