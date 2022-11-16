package com.example.Meetings_API.Repository;

import com.example.Meetings_API.Models.Meeting;
import com.example.Meetings_API.Services.MeetingsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MeetingsRepository {

    private final ObjectMapper mapper = new ObjectMapper();
    private final File file = new File("meetings.json");

    public MeetingsRepository() {
    }

    public List<Meeting> readMeetings() {
        try {
            return mapper.readValue(file, new TypeReference<ArrayList<Meeting>>() {});
//            meetings.setMeetings(mapper.readValue(file, new TypeReference<ArrayList<Meeting>>() {}));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<Meeting>();
        }
    }
    public void writeMeetings(List<Meeting> meetings) {
        try {
            mapper.writeValue(file, meetings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
