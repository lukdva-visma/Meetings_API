package com.example.Meetings_API.Models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class MeetingTest {

    @Test
    void doesContainPersonAsAttendee() {
        Date date = new Date();
        String name = "Test team meeting";
        String description = "This is a meeting of test team";
        Category category = Category.Hub;
        Type type = Type.Live;
        Person responsiblePerson = new Person("id1", "John Jones");
        Person attendee = new Person("id2", "John Smith");

        Meeting meeting = new Meeting(name, responsiblePerson, description, Category.Hub, Type.Live, date, date);
        meeting.addAttendee(attendee);
        assertTrue(meeting.doesContainPersonAsAttendee(attendee.getId()));
    }
    @Test
    void doesNotContainPersonAsAttendee() {
        Date date = new Date();
        String name = "Test team meeting";
        String description = "This is a meeting of test team";
        Category category = Category.Hub;
        Type type = Type.Live;
        Person responsiblePerson = new Person("id1", "John Jones");
        Person attendee = new Person("id2", "John Smith");

        Meeting meeting = new Meeting(name, responsiblePerson, description, Category.Hub, Type.Live, date, date);
        meeting.addAttendee(attendee);
        assertFalse(meeting.doesContainPersonAsAttendee(responsiblePerson.getId()));
    }

    @Test
    void addAttendee() {
        Date date = new Date();
        String name = "Test team meeting";
        String description = "This is a meeting of test team";
        Category category = Category.Hub;
        Type type = Type.Live;
        Person person = new Person("id1", "John Jones");
        Person attendee = new Person("id2", "John Smith");
        Meeting meeting = new Meeting(name, person, description, Category.Hub, Type.Live, date, date);
        meeting.addAttendee(attendee);

        ArrayList<Attendee> attendees = meeting.getAttendees();
        assertEquals(attendees.size(), 1);
        Attendee createdAttendee = attendees.get(0);
        assertNotEquals(createdAttendee.getId(), null);
        assertEquals(createdAttendee.getPerson(), attendee);
        assertTrue(createdAttendee.getAdded().after(date) || createdAttendee.getAdded().equals(date));
    }

    @Test
    void removeAttendee() {
        Date date = new Date();
        String name = "Test team meeting";
        String description = "This is a meeting of test team";
        Category category = Category.Hub;
        Type type = Type.Live;
        Person responsiblePerson = new Person("id1", "John Jones");
        Person attendee = new Person("id2", "John Smith");

        Meeting meeting = new Meeting(name, responsiblePerson, description, Category.Hub, Type.Live, date, date);
        meeting.addAttendee(attendee);
        ArrayList<Attendee> attendees = meeting.getAttendees();
        assertEquals(attendees.size(), 1);
        Attendee createdAttendee = attendees.get(0);
        meeting.removeAttendee(createdAttendee);
        assertEquals(attendees.size(), 0);
    }

    @Test
    void isAttendeeAvailable() {
        Date date = new Date();
        String name = "Test team meeting";
        String description = "This is a meeting of test team";
        Category category = Category.Hub;
        Type type = Type.Live;
        Person responsiblePerson = new Person("id1", "John Jones");
        Person attendee = new Person("id2", "John Smith");

        Meeting meeting = new Meeting(name, responsiblePerson, description, Category.Hub, Type.Live, date, date);
        meeting.addAttendee(attendee);
        Attendee createdAttendee = meeting.getAttendees().get(0);
        String attendeeId = createdAttendee.getId();
        meeting.removeAttendee(createdAttendee);
        assertFalse(meeting.isAttendeeAvailable(attendeeId));
    }
}