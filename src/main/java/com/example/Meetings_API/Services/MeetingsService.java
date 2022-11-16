package com.example.Meetings_API.Services;
import com.example.Meetings_API.Exceptions.BadRequestException;
import com.example.Meetings_API.Exceptions.NotFoundException;
import com.example.Meetings_API.Models.*;
import com.example.Meetings_API.Repository.MeetingsRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MeetingsService {

    private MeetingsRepository repository;
    @Getter @Setter
    private List<Meeting> meetings ;

    public MeetingsService() {}
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
        return (meeting1.getStartDate().before(meeting2.getEndDate()) && meeting1.getEndDate().after(meeting2.getStartDate()));
    }
    public Predicate<Meeting> byCategory(String category) {
        return meeting -> meeting.getCategory() == Category.valueOf(category);
    }
    public Predicate<Meeting> byType(String type) {
        return meeting -> meeting.getType() == Type.valueOf(type);
    }
    public Predicate<Meeting> byDescription(String description) {
        return meeting -> meeting.getDescription().toLowerCase().contains(description.toLowerCase());
    }
    public Predicate<Meeting> byResponsiblePerson(String id) {
        return meeting -> meeting.doesContainPersonAsAttendee(id);
    }
    public Predicate<Meeting> byStartDate(Date start) {
        return meeting -> meeting.getStartDate().after(start);
    }
    public Predicate<Meeting> byEndDate(Date end) {
        return meeting -> meeting.getEndDate().before(end);
    }
    public Predicate<Meeting> byAttendeesCount(int count) {
        return meeting -> meeting.getAttendees().size() >= count;
    }
    public List<Meeting> getFilteredMeetings (Optional<String> start, Optional<String> end, Optional<String> description, Optional<String> responsiblePersonId, Optional<String> category, Optional<String> type, Optional<String> attendees)
    {
        Stream<Meeting> stream = meetings.stream();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatterWithTime = new SimpleDateFormat("yyyy-MM-dd HH");

        if(description.isPresent())
            stream = stream.filter(byDescription(description.get()));
        if(responsiblePersonId.isPresent())
            stream = stream.filter(byResponsiblePerson(responsiblePersonId.get()));
        if(category.isPresent())
            stream = stream.filter(byCategory(category.get()));
        if(type.isPresent())
            stream = stream.filter(byType(type.get()));
        if(attendees.isPresent())
            stream = stream.filter(byAttendeesCount(Integer.parseInt(attendees.get())));
        if(start.isPresent())
            try {
                Date startDate = formatter.parse(start.get());
                stream = stream.filter(byStartDate(startDate));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        if(end.isPresent())
            try {
                Date endDate = formatterWithTime.parse(end.get() + " 24"); // Adding 24 hours so it includes whole day
                stream = stream.filter(byEndDate(endDate));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        List<Meeting> filteredMeetings = stream.collect(Collectors.toList());
        return filteredMeetings;
    }
}
