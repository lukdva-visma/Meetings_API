package com.example.Meetings_API.Controllers;

import com.example.Meetings_API.Assemblers.MeetingAssembler;
import com.example.Meetings_API.Assemblers.PersonAssembler;
import com.example.Meetings_API.Exceptions.UnauthorizedException;
import com.example.Meetings_API.Models.*;
import com.example.Meetings_API.Services.MeetingsService;
import com.example.Meetings_API.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
public class MeetingController {
    @Autowired
    MeetingsService meetingsService;
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    MeetingAssembler meetingAssembler;
    @Autowired
    PersonAssembler personAssembler;
    @PostMapping("/meetings")
    public Meeting createMeeting(@RequestBody MeetingDTO meetingDto) {
        Meeting meeting = meetingAssembler.mapMeeting(meetingDto);
        meetingsService.addMeeting(meeting);
        return meeting;
    }
    @DeleteMapping("/meetings/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMeeting(@RequestHeader (name="Authorization") String token, @PathVariable String id)
    {
        String personId = jwtUtils.getPersonIdFromToken(token);
        meetingsService.deleteMeeting(id, personId);
    }

    @PostMapping("/meetings/{id}/attendees")
    public Meeting addAttendee(@PathVariable String id, @RequestBody PersonDTO personDto) {
        Person person = personAssembler.mapPerson(personDto);
        return meetingsService.addPersonToMeeting(id, person);
    }

    @DeleteMapping("/meetings/{meetingId}/attendees/{attendeeId}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAttendee(@PathVariable String meetingId, @PathVariable String attendeeId) {
        Meeting updatedMeeting = meetingsService.removeAttendeeFromMeeting(meetingId, attendeeId);
    }

    @GetMapping("/meetings")
    public List<Meeting> getMeetings(MeetingsFilters filters) {
        return meetingsService.getFilteredMeetings(filters);
    }
}
