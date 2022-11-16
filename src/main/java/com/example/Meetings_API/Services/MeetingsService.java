package com.example.Meetings_API.Services;
import com.example.Meetings_API.Exceptions.BadRequestException;
import com.example.Meetings_API.Exceptions.NotFoundException;
import com.example.Meetings_API.Models.*;
import com.example.Meetings_API.Repository.MeetingsRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class MeetingsService {

    private MeetingsRepository repository;
    @Getter @Setter
    private List<Meeting> meetings ;

    public MeetingsService() {}
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
        return null;
    }
    public void removeMeeting(Meeting meeting)
    {
        meetings.remove(meeting);
        repository.writeMeetings(meetings);
    }
    public boolean isMeetingAvailable(String id) {
        if (getMeeting(id) == null)
            return false;
        return true;
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
    public MeetingsService filterByCategory(String category)
    {
        MeetingsService filteredMeetings = new MeetingsService();
        for (Meeting meeting: meetings) {
            if (meeting.getCategory() == Category.valueOf(category)){
                filteredMeetings.addMeeting(meeting);
            }
        }
        return filteredMeetings;
    }
    public MeetingsService filterByType(String type)
    {
        MeetingsService filteredMeetings = new MeetingsService();
        for (Meeting meeting: meetings) {
            if (meeting.getType() == Type.valueOf(type)){
                filteredMeetings.addMeeting(meeting);
            }
        }
        return filteredMeetings;
    }
    public MeetingsService filterByDescription(String description) {
        MeetingsService filteredMeetings = new MeetingsService();
        for (Meeting meeting: meetings) {
            if (meeting.getDescription().toLowerCase().contains(description.toLowerCase())){
                filteredMeetings.addMeeting(meeting);
            }
        }
        return filteredMeetings;

    }
    public MeetingsService filterByResponsiblePerson(String id)
    {
        MeetingsService filteredMeetings = new MeetingsService();
        for (Meeting meeting: meetings) {
            if (meeting.doesContainPersonAsAttendee(id)){
                filteredMeetings.addMeeting(meeting);
            }
        }
        return filteredMeetings;
    }
    public MeetingsService filterByStartDate (Date start)
    {
        MeetingsService filteredMeetings = new MeetingsService();
        for (Meeting meeting: meetings) {
            if (meeting.getStartDate().after(start)){
                filteredMeetings.addMeeting(meeting);
            }
        }
        return filteredMeetings;
    }
    public MeetingsService filterByEndDate (Date end)
    {
        MeetingsService filteredMeetings = new MeetingsService();
        for (Meeting meeting: meetings) {
            if (meeting.getEndDate().before(end)){
                filteredMeetings.addMeeting(meeting);
            }
        }
        return filteredMeetings;
    }
    public MeetingsService filterByAttendeesCount (int count)
    {
        MeetingsService filteredMeetings = new MeetingsService();
        for (Meeting meeting: meetings) {
            if (meeting.getAttendees().size() >= count){
                filteredMeetings.addMeeting(meeting);
            }
        }
        return filteredMeetings;
    }
}
