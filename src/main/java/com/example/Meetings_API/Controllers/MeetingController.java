package com.example.Meetings_API.Controllers;

import com.example.Meetings_API.Models.Meeting;
import com.example.Meetings_API.Models.Meetings;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeetingController {
    Meetings meetings = new Meetings();

    @PostMapping("/meetings")
    public Meeting createMeeting(@RequestBody Meeting meeting) {
        meetings.addMeeting(meeting);
        return meeting;
    }
}
