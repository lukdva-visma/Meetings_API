package com.example.Meetings_API.Models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
public class Meeting {
    private String id;
    private String name;
    private Person responsiblePerson;
    private String description;
    private Category category;
    private Type type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Attendee> attendees = new ArrayList<>();

    public Meeting() {
        this.id = UUID.randomUUID().toString();
    }
    public Meeting(String name, Person responsiblePerson, String description, Category category, Type type, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.responsiblePerson = responsiblePerson;
        this.description = description;
        this.category = category;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
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
       return getAttendee(id) != null;
   }
}
