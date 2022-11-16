package com.example.Meetings_API.Controllers;

import com.example.Meetings_API.Exceptions.BadRequestException;
import com.example.Meetings_API.Exceptions.UnauthorizedException;
import com.example.Meetings_API.Models.Attendee;
import com.example.Meetings_API.Models.Meeting;
import com.example.Meetings_API.Services.MeetingsService;
import com.example.Meetings_API.Exceptions.NotFoundException;
import com.example.Meetings_API.Models.Person;
import com.example.Meetings_API.Repository.MeetingsRepository;
import com.example.Meetings_API.Utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class MeetingController {


    MeetingsService meetings = new MeetingsService();
    MeetingsRepository meetingsRepository = new MeetingsRepository();
    JwtUtils jwtUtils = new JwtUtils();
    @ModelAttribute
    public void readFile() {
        meetingsRepository.readMeetings();
    }
    @PostMapping("/meetings")
    public Meeting createMeeting(@RequestBody Meeting meeting) {
        meetings.addAttendeeToMeeting(meeting, meeting.getResponsiblePerson());
        meetings.addMeeting(meeting);
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
            return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        }
        throw new UnauthorizedException();
    }

    @PostMapping("/meetings/{id}/attendees")
    public Meeting addAttendee(@PathVariable String id, @RequestBody Person person) {
        if (!meetings.isMeetingAvailable(id))
            throw new NotFoundException();
        Meeting meeting = meetings.getMeeting(id);
        Meeting updatedMeeting = meetings.addAttendeeToMeeting(meeting, person);
        meetings.updateMeeting(updatedMeeting);
        //Check if works correctly
        return meeting;
    }

    @DeleteMapping("/meetings/{meetingId}/attendees/{attendeeId}")
    public ResponseEntity removeAttendee(@PathVariable String meetingId, @PathVariable String attendeeId) {
        if (!meetings.isMeetingAvailable(meetingId))
            throw new NotFoundException("Meeting not found");
        Meeting meeting = meetings.getMeeting(meetingId);
        Meeting updatedMeeting = meetings.removeAttendeeFromMeeting(meeting, attendeeId);
        meetings.updateMeeting(updatedMeeting);
        return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/meetings")
    public List<Meeting> getMeetings(@RequestParam Optional<String> start, @RequestParam Optional<String> end, @RequestParam Optional<String> description, @RequestParam Optional<String> responsiblePersonId, @RequestParam Optional<String> category, @RequestParam Optional<String> type, @RequestParam Optional<String> attendees) {
        meetingsRepository.readMeetings();
        MeetingsService filteredMeetings = meetings;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatterWithTime = new SimpleDateFormat("yyyy-MM-dd HH");
        if(description.isPresent())
            filteredMeetings = filteredMeetings.filterByDescription(description.get());
        if(responsiblePersonId.isPresent())
            filteredMeetings = filteredMeetings.filterByResponsiblePerson(responsiblePersonId.get());
        if(category.isPresent())
            filteredMeetings = filteredMeetings.filterByCategory(category.get());
        if(type.isPresent())
            filteredMeetings = filteredMeetings.filterByType(type.get());
        if(attendees.isPresent())
            filteredMeetings = filteredMeetings.filterByAttendeesCount(Integer.parseInt(attendees.get()));
        if(start.isPresent())
            try {
                Date startDate = formatter.parse(start.get());
                filteredMeetings = filteredMeetings.filterByStartDate(startDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        if(end.isPresent())
            try {
                Date endDate = formatterWithTime.parse(end.get() + " 24"); // Adding 24 hours so it includes whole day
                filteredMeetings = filteredMeetings.filterByEndDate(endDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        return filteredMeetings.getMeetings();
    }
}
