package com.example.Meetings_API.Services;
import com.example.Meetings_API.Exceptions.BadRequestException;
import com.example.Meetings_API.Exceptions.NotFoundException;
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
    private List<Meeting> meetings ;
    @Autowired
    public MeetingsService(MeetingsRepository repository) {
        this.repository = repository;
        this.meetings = this.repository.readMeetings();
    }
    public void addMeeting(Meeting meeting) {
        meetings.add(meeting);
        repository.writeMeetings(meetings);
    }
    public Meeting getMeeting(String id)
    {
        for (Meeting meeting: meetings
             ) {
            if(meeting.getId().equals(id))
                return meeting;
        }
        throw new NotFoundException("Meeting not foud");
    }
    public void removeMeeting(Meeting meeting)
    {
        meetings.remove(meeting);
        repository.writeMeetings(meetings);
    }
    public List<Meeting> listOfMeetingsPersonIsIn(Person person) {
        List<Meeting> list = new ArrayList<>();
        for (Meeting meeting: meetings) {
            if(meeting.doesContainPersonAsAttendee(person.getId()))
                list.add(meeting);
        }
        return list;
    }
    public boolean personHasConflictingMeetings(Person person, Meeting meetingToBeAttended) {
        List<Meeting> list = listOfMeetingsPersonIsIn(person);
        for (Meeting meeting: list) {
            if (dateRangesOverlap(meeting, meetingToBeAttended))
                return true;
        }
        return false;

    }
    public Meeting addAttendeeToMeeting(Meeting meeting, Person person) {
        if(meeting.doesContainPersonAsAttendee(person.getId()))
            throw new BadRequestException("Person already added to meeting");
        if (personHasConflictingMeetings(person, meeting))
            throw  new BadRequestException("Person has conflicting meetings");
        meeting.addAttendee(person);
        return meeting;
    }
    public Meeting removeAttendeeFromMeeting(Meeting meeting, String attendeeId){
        if (!meeting.isAttendeeAvailable(attendeeId))
            throw new NotFoundException("Attendee not found");
        Attendee attendee = meeting.getAttendee(attendeeId);
        if(attendee.getPerson().getId().equals(meeting.getResponsiblePerson().getId()))
            throw new BadRequestException("Cannot remove responsible person from meeting");
        meeting.removeAttendee(attendee);
        return meeting;
    }
    public void updateMeeting(Meeting meeting)
    {
        int index = meetings.indexOf(meeting);
        meetings.set(index, meeting);
        repository.writeMeetings(meetings);
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
        return meeting -> meeting.doesContainPersonAsAttendee(id);
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
    public List<Meeting> getFilteredMeetings (MeetingsFilters filters)
    {
        Stream<Meeting> stream = meetings.stream();

        if(filters.getDescription() != null)
            stream = stream.filter(byDescription(filters.getDescription()));
        if(filters.getResponsiblePersonId() != null)
            stream = stream.filter(byResponsiblePerson(filters.getResponsiblePersonId()));
        if(filters.getCategory() != null)
            stream = stream.filter(byCategory(filters.getCategory()));
        if(filters.getType() != null)
            stream = stream.filter(byType(filters.getType()));
        if(filters.getAttendees() != null)
            stream = stream.filter(byAttendeesCount(filters.getAttendees()));
        if(filters.getStart() != null)
                stream = stream.filter(byStartDate(filters.getStart().atStartOfDay()));
        if(filters.getEnd() != null)
                stream = stream.filter(byEndDate(filters.getEnd().plusDays(1).atStartOfDay()));

        List<Meeting> filteredMeetings = stream.collect(Collectors.toList());
        return filteredMeetings;
    }
}
