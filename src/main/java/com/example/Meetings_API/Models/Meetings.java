package com.example.Meetings_API.Models;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.Date;

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
            if(meeting.doesContainPersonAsAttendee(person.getId()))
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
    public Meetings filterByCategory(String category)
    {
        Meetings filteredMeetings = new Meetings();
        for (Meeting meeting: meetings) {
            if (meeting.getCategory() == Category.valueOf(category)){
                filteredMeetings.addMeeting(meeting);
            }
        }
        return filteredMeetings;
    }
    public Meetings filterByType(String type)
    {
        Meetings filteredMeetings = new Meetings();
        for (Meeting meeting: meetings) {
            if (meeting.getType() == Type.valueOf(type)){
                filteredMeetings.addMeeting(meeting);
            }
        }
        return filteredMeetings;
    }
    public Meetings filterByDescription(String description) {
        Meetings filteredMeetings = new Meetings();
        for (Meeting meeting: meetings) {
            if (meeting.getDescription().toLowerCase().contains(description.toLowerCase())){
                filteredMeetings.addMeeting(meeting);
            }
        }
        return filteredMeetings;

    }
    public Meetings filterByResponsiblePerson(String id)
    {
        Meetings filteredMeetings = new Meetings();
        for (Meeting meeting: meetings) {
            if (meeting.doesContainPersonAsAttendee(id)){
                filteredMeetings.addMeeting(meeting);
            }
        }
        return filteredMeetings;
    }
//    public Meetings filterByDates (Date start, Date end)
//    {
//        Meetings filteredMeetings = new Meetings();
//        for (Meeting meeting: meetings) {
//            if (start.meeting.getType() == Type.valueOf(type)){
//                filteredMeetings.addMeeting(meeting);
//            }
//        }
//        return filteredMeetings;
//    }
    public Meetings filterByAttendeesCount (int count)
    {
        Meetings filteredMeetings = new Meetings();
        for (Meeting meeting: meetings) {
            if (meeting.getAttendees().size() >= count){
                filteredMeetings.addMeeting(meeting);
            }
        }
        return filteredMeetings;
    }
}
