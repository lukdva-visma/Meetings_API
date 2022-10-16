package com.example.Meetings_API.Models;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;

public class Meetings {
    @Getter @Setter
    private ArrayList<Meeting> meetings = new ArrayList<>();

    public Meetings() {}
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
    public ArrayList<Meeting> listOfMeetingsPersonIsIn(Person person) {
        ArrayList<Meeting> list = new ArrayList<>();
        for (Meeting meeting: meetings) {
            if(meeting.doesContainPersonAsAttendee(person))
                list.add(meeting);
        }
        return list;
    }
    public boolean personHasConflictingMeetings(Person person, Meeting meetingToBeAttended) {
        ArrayList<Meeting> list = listOfMeetingsPersonIsIn(person);
        for (Meeting meeting: list) {
            if (dateRangesOverlap(meeting, meetingToBeAttended))
                return true;
        }
        return false;

    }
    public boolean dateRangesOverlap(Meeting meeting1, Meeting meeting2) {
        return (meeting1.getStartDate().before(meeting2.getEndDate()) && meeting1.getEndDate().after(meeting2.getStartDate()));
    }
}
