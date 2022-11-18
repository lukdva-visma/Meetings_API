package com.example.Meetings_API.Controllers;

import com.example.Meetings_API.Exceptions.UnauthorizedException;
import com.example.Meetings_API.Models.Meeting;
import com.example.Meetings_API.Models.MeetingsFilters;
import com.example.Meetings_API.Services.MeetingsService;
import com.example.Meetings_API.Models.Person;
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

    @PostMapping("/meetings")
    public Meeting createMeeting(@RequestBody Meeting meeting) {
        meetingsService.addAttendeeToMeeting(meeting, meeting.getResponsiblePerson());
        meetingsService.addMeeting(meeting);
        return meeting;
    }
    @DeleteMapping("/meetings/{id}")
    public ResponseEntity deleteMeeting(@RequestHeader (name="Authorization") String token, @PathVariable String id)
    {
        Meeting meeting = meetingsService.getMeeting(id);
        String personId = jwtUtils.getPersonIdFromToken(token);
        if(meeting.getResponsiblePerson().getId().equals(personId))
        {
            meetingsService.removeMeeting(meeting);
            return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        }
        throw new UnauthorizedException();
    }

    @PostMapping("/meetings/{id}/attendees")
    public Meeting addAttendee(@PathVariable String id, @RequestBody Person person) {
        Meeting meeting = meetingsService.getMeeting(id);
        Meeting updatedMeeting = meetingsService.addAttendeeToMeeting(meeting, person);
        meetingsService.updateMeeting(updatedMeeting);
        return meeting;
    }

    @DeleteMapping("/meetings/{meetingId}/attendees/{attendeeId}")
    public ResponseEntity removeAttendee(@PathVariable String meetingId, @PathVariable String attendeeId) {
        Meeting meeting = meetingsService.getMeeting(meetingId);
        Meeting updatedMeeting = meetingsService.removeAttendeeFromMeeting(meeting, attendeeId);
        meetingsService.updateMeeting(updatedMeeting);
        return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/meetings")
    public List<Meeting> getMeetings(MeetingsFilters filters) {
        return meetingsService.getFilteredMeetings(filters);
    }
}
