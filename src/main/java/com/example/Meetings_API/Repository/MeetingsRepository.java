package com.example.Meetings_API.Repository;

import com.example.Meetings_API.Models.Meeting;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MeetingsRepository {

    private final ObjectMapper mapper;
    private final File file;

    @Autowired
    public MeetingsRepository(ObjectMapper objectMapper, @Qualifier("MeetingsFile") File file) {

        this.mapper = objectMapper;
        this.file = file;
    }

    public List<Meeting> readMeetings() {
        try {
            if (!file.exists()) {
                file.createNewFile();
                mapper.writeValue(file, new ArrayList<>());
            }
            return mapper.readValue(file, new TypeReference<ArrayList<Meeting>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
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
