package com.example.Meetings_API.Controllers;

import com.example.Meetings_API.Exceptions.BadRequestException;
import com.example.Meetings_API.Exceptions.UnauthorizedException;
import com.example.Meetings_API.Models.Attendee;
import com.example.Meetings_API.Models.Meeting;
import com.example.Meetings_API.Models.Meetings;
import com.example.Meetings_API.Exceptions.NotFoundException;
import com.example.Meetings_API.Models.Person;
import com.example.Meetings_API.Services.MeetingsService;
import com.example.Meetings_API.Utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MeetingController {


    Meetings meetings = new Meetings();
    MeetingsService meetingsService = new MeetingsService(meetings);
    JwtUtils jwtUtils = new JwtUtils();
    @ModelAttribute
    public void readFile() {
        meetingsService.readMeetings();
    }
    @PostMapping("/meetings")
    public Meeting createMeeting(@RequestBody Meeting meeting) {
        meetings.addMeeting(meeting);
        addAttendee(meeting.getId(), meeting.getResponsiblePerson());
        meetingsService.writeMeetings();
        return meeting;
    }

    @DeleteMapping("/meetings/{id}")
    public ResponseEntity deleteMeeting(@RequestHeader (name="Authorization") String token, @PathVariable String id)
    {
        Meeting meeting = meetings.getMeeting(id);
        if(meeting == null) {
            throw new NotFoundException();
        }
        String personId = jwtUtils.getPersonIdFromToken(token);
        if(meeting.getResponsiblePerson().getId().equals(personId))
        {
            meetings.removeMeeting(meeting);
            meetingsService.writeMeetings();
            return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        }
        throw new UnauthorizedException();
    }

    @PostMapping("/meetings/{id}/attendees")
    public Meeting addAttendee(@PathVariable String id, @RequestBody Person person) {
        if (!meetings.isMeetingAvailable(id))
            throw new NotFoundException();
        Meeting meeting = meetings.getMeeting(id);
        if(meeting.doesContainPersonAsAttendee(person))
            throw new BadRequestException("Person already added to meeting");
        if (meetings.personHasConflictingMeetings(person, meeting))
            throw  new BadRequestException("Person has conflicting meetings");
        meeting.addAttendee(person);
        meetingsService.writeMeetings();
        return meeting;
    }

    @DeleteMapping("/meetings/{meetingId}/attendees/{attendeeId}")
    public ResponseEntity removeAttendee(@PathVariable String meetingId, @PathVariable String attendeeId) {
        if (!meetings.isMeetingAvailable(meetingId))
            throw new NotFoundException("Meeting not found");
        Meeting meeting = meetings.getMeeting(meetingId);
        if (!meeting.isAttendeeAvailable(attendeeId))
            throw new NotFoundException("Attendee not found");
        Attendee attendee = meeting.getAttendee(attendeeId);
        if(attendee.getPerson().getId().equals(meeting.getResponsiblePerson().getId()))
            throw new BadRequestException("Cannot remove responsible person from meeting");
        meeting.removeAttendee(attendee);
        meetingsService.writeMeetings();
        return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
    }
}
