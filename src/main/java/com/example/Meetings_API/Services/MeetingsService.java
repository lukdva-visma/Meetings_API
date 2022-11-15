package com.example.Meetings_API.Services;
import com.example.Meetings_API.Models.Category;
import com.example.Meetings_API.Models.Meeting;
import com.example.Meetings_API.Models.Person;
import com.example.Meetings_API.Models.Type;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class MeetingsService {
    @Getter @Setter
    private List<Meeting> meetings = new ArrayList<>();

    public MeetingsService() {}
    public void addMeeting(Meeting meeting) {
        meetings.add(meeting);
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
