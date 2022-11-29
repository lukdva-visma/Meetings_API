package com.example.Meetings_API.Services;

import com.example.Meetings_API.Exceptions.BadRequestException;
import com.example.Meetings_API.Exceptions.NotFoundException;
import com.example.Meetings_API.Exceptions.UnauthorizedException;
import com.example.Meetings_API.Models.*;
import com.example.Meetings_API.Repository.MeetingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MeetingsService {

    private MeetingsRepository repository;
    private List<Meeting> meetings;

    @Autowired
    public MeetingsService(MeetingsRepository repository) {
        this.repository = repository;
        this.meetings = this.repository.readMeetings();
    }

    public void addMeeting(Meeting meeting) {
        if (personHasConflictingMeetings(meeting.getResponsiblePerson(), meeting))
            throw new BadRequestException("Person has conflicting meetings");
        meetings.add(meeting);
        repository.writeMeetings(meetings);
    }

    public Meeting getMeeting(String id) {
        return meetings.stream().filter(m -> m.getId().equals(id)).findFirst().orElseThrow(() -> new NotFoundException("Meeting not found"));
    }

    public void removeMeeting(Meeting meeting) {
        meetings.remove(meeting);
        repository.writeMeetings(meetings);
    }

    public List<Meeting> listOfMeetingsPersonIsIn(Person person) {
        List<Meeting> list = new ArrayList<>();
        for (Meeting meeting : meetings) {
            if (meeting.doesContainPersonAsAttendee(person.getId()))
                list.add(meeting);
        }
        return list;
    }

    public boolean personHasConflictingMeetings(Person person, Meeting meetingToBeAttended) {
        List<Meeting> list = listOfMeetingsPersonIsIn(person);
        for (Meeting meeting : list) {
            if (dateRangesOverlap(meeting, meetingToBeAttended))
                return true;
        }
        return false;

    }

    public Meeting addPersonToMeeting(String meetingId, Person person) {
        Meeting meeting = getMeeting(meetingId);
        if (meeting.doesContainPersonAsAttendee(person.getId()))
            throw new BadRequestException("Person already added to meeting");
        if (personHasConflictingMeetings(person, meeting))
            throw new BadRequestException("Person has conflicting meetings");
        meeting.addAttendee(person);
        repository.writeMeetings(meetings);
        return meeting;
    }

    public Meeting removeAttendeeFromMeeting(String meetingId, String attendeeId) {
        Meeting meeting = getMeeting(meetingId);
        if (!meeting.isAttendeeAvailable(attendeeId))
            throw new NotFoundException("Attendee not found");
        Attendee attendee = meeting.getAttendee(attendeeId);
        if (attendee.getPerson().getId().equals(meeting.getResponsiblePerson().getId()))
            throw new BadRequestException("Cannot remove responsible person from meeting");
        meeting.removeAttendee(attendee);
        repository.writeMeetings(meetings);
        return meeting;
    }

    public void deleteMeeting(String meetingId, String personId) {
        Meeting meeting = getMeeting(meetingId);
        if (!meeting.getResponsiblePerson().getId().equals(personId))
            throw new UnauthorizedException();
        removeMeeting(meeting);
    }

    public boolean dateRangesOverlap(Meeting meeting1, Meeting meeting2) {
        return (meeting1.getStartDate().isBefore(meeting2.getEndDate()) && meeting1.getEndDate().isAfter(meeting2.getStartDate()));
    }

    private Predicate<Meeting> byCategory(String category) {
        return meeting -> meeting.getCategory() == Category.valueOf(category);
    }

    private Predicate<Meeting> byType(String type) {
        return meeting -> meeting.getType() == Type.valueOf(type);
    }

    private Predicate<Meeting> byDescription(String description) {
        return meeting -> meeting.getDescription().toLowerCase().contains(description.toLowerCase());
    }

    private Predicate<Meeting> byResponsiblePerson(String id) {
        return meeting -> meeting.getResponsiblePerson().getId().equals(id);
    }

    private Predicate<Meeting> byStartDate(LocalDateTime start) {
        return meeting -> meeting.getStartDate().isAfter(start);
    }

    private Predicate<Meeting> byEndDate(LocalDateTime end) {
        return meeting -> meeting.getEndDate().isBefore(end);
    }

    private Predicate<Meeting> byAttendeesCount(int count) {
        return meeting -> meeting.getAttendees().size() >= count;
    }

    public List<Meeting> getFilteredMeetings(MeetingFilter filters) {
        Stream<Meeting> stream = meetings.stream();

        if (filters.getDescription() != null)
            stream = stream.filter(byDescription(filters.getDescription()));
        if (filters.getResponsiblePersonId() != null)
            stream = stream.filter(byResponsiblePerson(filters.getResponsiblePersonId()));
        if (filters.getCategory() != null)
            stream = stream.filter(byCategory(filters.getCategory()));
        if (filters.getType() != null)
            stream = stream.filter(byType(filters.getType()));
        if (filters.getAttendees() != null)
            stream = stream.filter(byAttendeesCount(filters.getAttendees()));
        if (filters.getStart() != null)
            stream = stream.filter(byStartDate(filters.getStart().atStartOfDay()));
        if (filters.getEnd() != null)
            stream = stream.filter(byEndDate(filters.getEnd().plusDays(1).atStartOfDay()));

        List<Meeting> filteredMeetings = stream.collect(Collectors.toList());
        return filteredMeetings;
    }
}
