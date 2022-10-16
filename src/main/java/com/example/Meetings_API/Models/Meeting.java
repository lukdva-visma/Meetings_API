package com.example.Meetings_API.Models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Meeting {
    @Getter @Setter
    private String id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private Person responsiblePerson;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private Category category;
    @Getter @Setter
    private Type type;
    @Getter @Setter
    private Date startDate;
    @Getter @Setter
    private Date endDate;
    @Getter @Setter
    private ArrayList<Attendee> attendees = new ArrayList<>();

    public Meeting() {
        this.id = UUID.randomUUID().toString();
    }
    public boolean doesContainPersonAsAttendee(String id)
    {
        for (Attendee attendee: attendees) {
            if (attendee.getPerson().getId().equals(id))
                return true;
        }
        return false;
    }

   public void addAttendee(Person person)
   {
       Attendee attendee = new Attendee(person);
       attendees.add(attendee);
   }
   public Attendee getAttendee(String id){
       for (Attendee attendee: attendees) {
           if(attendee.getId().equals(id))
               return attendee;
       }
       return null;
   }
   public void removeAttendee(Attendee attendee)
   {
       attendees.remove(attendee);
   }
   public boolean isAttendeeAvailable(String id)
   {
       if(getAttendee(id) == null)
           return false;
       return true;
   }
}
