package com.example.Meetings_API.Models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Meetings {
    @Getter
    private ArrayList<Meeting> meetings = new ArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final File file = new File("meetings.json");

    private void readMeetings() {
        try {
            meetings = mapper.readValue(file, new TypeReference<ArrayList<Meeting>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void writeMeetings() {
        try {
            mapper.writeValue(file, meetings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Meetings() {
        readMeetings();
    }
    public void addMeeting( Meeting meeting) {
        readMeetings();
        meetings.add(meeting);
        writeMeetings();
    }
}
