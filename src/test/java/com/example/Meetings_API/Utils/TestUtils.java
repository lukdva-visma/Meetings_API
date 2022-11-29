package com.example.Meetings_API.Utils;

import com.example.Meetings_API.Models.Meeting;
import com.example.Meetings_API.Repository.MeetingsRepository;
import com.example.Meetings_API.Services.MeetingsService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

public class TestUtils {

    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public static MeetingsService getMeetingServiceWithInjectedStubbedRepository(MeetingsRepository repository, List<Meeting> meetings) {
        when(repository.readMeetings()).thenReturn(meetings);
        return new MeetingsService(repository);
    }

    public static MeetingsService getMeetingServiceWithInjectedStubbedRepository(MeetingsRepository repository, Meeting meeting) {
        List<Meeting> meetings = new ArrayList<>(List.of(meeting));
        when(repository.readMeetings()).thenReturn(meetings);
        return new MeetingsService(repository);
    }
}
