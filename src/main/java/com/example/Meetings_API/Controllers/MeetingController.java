package com.example.Meetings_API.Controllers;

import com.example.Meetings_API.Exceptions.UnauthorizedException;
import com.example.Meetings_API.Models.Meeting;
import com.example.Meetings_API.Models.Meetings;
import com.example.Meetings_API.Exceptions.NotFoundException;
import com.example.Meetings_API.Utils.JwtUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MeetingController {

    Meetings meetings = new Meetings();
    JwtUtils jwtUtils = new JwtUtils();
    @PostMapping("/meetings")
    public Meeting createMeeting(@RequestBody Meeting meeting) {
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
}
