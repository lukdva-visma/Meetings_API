package com.example.Meetings_API.Services;

import com.example.Meetings_API.Models.Meeting;
import com.example.Meetings_API.Models.Meetings;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MeetingsService {

    private final ObjectMapper mapper = new ObjectMapper();
    private final File file = new File("meetings.json");

    private Meetings meetings;

    public MeetingsService(Meetings meetings) {
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
