package com.example.Meetings_API.Repository;

import com.example.Meetings_API.Models.Meeting;
import com.example.Meetings_API.Services.MeetingsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Repository
public class MeetingsRepository {

    private final ObjectMapper mapper = new ObjectMapper();
    private final File file = new File("meetings.json");

    private MeetingsService meetings;

    public MeetingsRepository(MeetingsService meetings) {
        this.meetings = meetings;
    }

    public void readMeetings() {
        try {
            meetings.setMeetings(mapper.readValue(file, new TypeReference<ArrayList<Meeting>>() {}));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeMeetings() {
        try {
            mapper.writeValue(file, meetings.getMeetings());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
