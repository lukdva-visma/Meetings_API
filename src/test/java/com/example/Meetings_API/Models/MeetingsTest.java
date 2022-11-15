package com.example.Meetings_API.Models;

import com.example.Meetings_API.Services.MeetingsService;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeetingsTest {
    Meeting meeting;
    Person responsiblePerson;
    @BeforeEach
    public void createMeeting() {
        Date start = new Date(1760695200); //2025-10-17 10:00:00
        Date end = new Date(1760698800); //2025-10-17 11:00:00
        String name = "Test team meeting";
        String description = "This is a meeting of test team";
        Category category = Category.Hub;
        Type type = Type.Live;
        responsiblePerson = new Person("id1", "John Jones");
        meeting = new Meeting(name, responsiblePerson, description, category, type, start, end);
    }
    @Test
    void verifyAddMeetingMethodAddsMeetingMeetingsList() {
        Meeting[] meetingsList = {meeting};
        MeetingsService meetings = new MeetingsService();
        meetings.addMeeting(meeting);
        assertArrayEquals(meetings.getMeetings().toArray(), meetingsList);
    }

    @Test
    void getExistingMeetingReturnsAddedMeeting() {
        MeetingsService meetings = new MeetingsService();
        meetings.addMeeting(meeting);
        Meeting retrievedMeeting = meetings.getMeeting(meeting.getId());
        assertEquals(retrievedMeeting, meeting);
    }

    @Test
    void tryGettingMeetingNotInTheList() {
        MeetingsService meetings = new MeetingsService();
        Meeting retrievedMeeting = meetings.getMeeting(meeting.getId());
        assertEquals(retrievedMeeting, null);
    }

    @Test
    void checkMeetingIsRemovedSuccessfully() {
        MeetingsService meetings = new MeetingsService();
        meetings.addMeeting(meeting);
        meetings.removeMeeting(meeting);
        assertEquals(meetings.getMeetings().size(), 0);
    }

    @Test
    void isMeetingAvailableReturnsTrueWhenMeetingIsPresent() {
        MeetingsService meetings = new MeetingsService();
        meetings.addMeeting(meeting);
        assertTrue(meetings.isMeetingAvailable(meeting.getId()));
    }
    @Test
    void isMeetingAvailableReturnsFalseWhenMeetingIsNotPresent() {
        MeetingsService meetings = new MeetingsService();
        assertFalse(meetings.isMeetingAvailable(meeting.getId()));
    }

    @Test
    void listOfMeetingsPersonIsInWhenPersonHasNoMeetings() {
        //empty list
        Meeting[] emptyArray= {};
        MeetingsService meetings = new MeetingsService();
        meeting.addAttendee(responsiblePerson);
        meetings.addMeeting(meeting);
        Person attendee = new Person("id2", "John Smith");
        List<Meeting> listOfMeetings = meetings.listOfMeetingsPersonIsIn(attendee);
        assertArrayEquals(listOfMeetings.toArray(), emptyArray);
        // two meetings, returns one
    }
    @Test
    void listOfMeetingsPersonIsInWhenPersonAttendsOneOfTheMeetings() {
        Date date = new Date();
        String name = "Unit Test meeting";
        String description = "This is a meeting of unit tests";
        Category category = Category.CodeMonkey;
        Type type = Type.InPerson;
        Meeting meetingPersonIsIn = new Meeting(name, responsiblePerson, description, category, type, date, date);
        Person attendee = new Person("id2", "John Smith");
        meetingPersonIsIn.addAttendee(attendee);

        Meeting[] emptyArray= {meetingPersonIsIn};
        MeetingsService meetings = new MeetingsService();
        meeting.addAttendee(responsiblePerson);
        meetings.addMeeting(meeting);
        meetings.addMeeting(meetingPersonIsIn);

        List<Meeting> listOfMeetings = meetings.listOfMeetingsPersonIsIn(attendee);
        assertArrayEquals(listOfMeetings.toArray(), emptyArray);
    }
    @Test
    void personHasConflictingMeetingsReturnsTrue() {
        Date start = new Date(1760697000); //2025-10-17 10:30:00
        Date end = new Date(1760698800); //2025-10-17 11:00:00
        String name = "I Overlap";
        String description = "This definitely overlaps";
        Category category = Category.Short;
        Type type = Type.Live;
        Meeting overlappingMeeting = new Meeting(name, responsiblePerson, description, category, type, start, end);

        Person personWithConflictingMeetings = new Person("id123", "Mr. Conflict");
        meeting.addAttendee(personWithConflictingMeetings);
        MeetingsService meetings = new MeetingsService();
        meetings.addMeeting(meeting);
        meetings.addMeeting(overlappingMeeting);
        assertTrue(meetings.personHasConflictingMeetings(personWithConflictingMeetings, overlappingMeeting));
    }
    @Test
    void personHasConflictingMeetingsReturnsFalse() {
        Date start = new Date(1760698800); //2025-10-17 11:00:00
        Date end = new Date(1760702400); //2025-10-17 12:00:00
        String name = "I Overlap";
        String description = "This definitely overlaps";
        Category category = Category.Short;
        Type type = Type.Live;
        Meeting nonOverlappingMeeting = new Meeting(name, responsiblePerson, description, category, type, start, end);

        Person personWithoutConflictingMeetings = new Person("id123", "Mr. Conflict");
        meeting.addAttendee(personWithoutConflictingMeetings);
        MeetingsService meetings = new MeetingsService();
        meetings.addMeeting(meeting);
        meetings.addMeeting(nonOverlappingMeeting);
        assertFalse(meetings.personHasConflictingMeetings(personWithoutConflictingMeetings, nonOverlappingMeeting));
    }

    @Test
    void dateRangesOverlapReturnTrue() {
        Date start1 = new Date(1760695200); //2025-10-17 10:00:00
        Date end1 = new Date(1760698800); //2025-10-17 11:00:00
        Meeting meeting1 =  new Meeting();
        meeting1.setStartDate(start1);
        meeting1.setEndDate(end1);

        Date start2 = new Date(1760697000); //2025-10-17 10:30:00
        Date end2 = new Date(1760702400); //2025-10-17 12:00:00
        Meeting meeting2 =  new Meeting();
        meeting2.setStartDate(start2);
        meeting2.setEndDate(end2);
        MeetingsService meetings = new MeetingsService();

        assertTrue(meetings.dateRangesOverlap(meeting1, meeting2));
    }
    @Test
    void dateRangesOverlapReturnFalse() {
        Date start1 = new Date(1760695200); //2025-10-17 10:00:00
        Date end1 = new Date(1760698800); //2025-10-17 11:00:00
        Meeting meeting1 =  new Meeting();
        meeting1.setStartDate(start1);
        meeting1.setEndDate(end1);

        Date start2 = new Date(1760700600); //2025-10-17 11:30:00
        Date end2 = new Date(1760702400); //2025-10-17 12:00:00
        Meeting meeting2 =  new Meeting();
        meeting2.setStartDate(start2);
        meeting2.setEndDate(end2);
        MeetingsService meetings = new MeetingsService();

        assertFalse(meetings.dateRangesOverlap(meeting1, meeting2));
    }
    @Test
    void dateRangesOverlapReturnFalseWhenBoundariesTouch() {
        Date start1 = new Date(1760695200); //2025-10-17 10:00:00
        Date end1 = new Date(1760698800); //2025-10-17 11:00:00
        Meeting meeting1 =  new Meeting();
        meeting1.setStartDate(start1);
        meeting1.setEndDate(end1);

        Date start2 = new Date(1760698800); //2025-10-17 11:00:00
        Date end2 = new Date(1760702400); //2025-10-17 12:00:00
        Meeting meeting2 =  new Meeting();
        meeting2.setStartDate(start2);
        meeting2.setEndDate(end2);
        MeetingsService meetings = new MeetingsService();

        assertFalse(meetings.dateRangesOverlap(meeting1, meeting2));
    }

    //Group these
    //Create before each, and create meetings object with multiple meetings
    @Nested
    @DisplayName("Tests for filtering meetings")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class TestsFilteringMeetings {
        MeetingsService meetings;
        Meeting meeting1;
        Meeting meeting2;
        @BeforeAll
        public void createMeeting() {
            Date start = new Date(1760695200); //2025-10-17 10:00:00
            Date end = new Date(1760873400); //2025-10-19 11:30:00
            String name = "Test team meeting";
            String description = "This is a meeting of test team";
            Category category = Category.Hub;
            Type type = Type.Live;
            Person responsiblePerson1 = new Person("ID1", "Michael Carter");
            Person attendee1 = new Person("ID5", "Michael Gary");
            Person attendee2 = new Person("ID6", "Michael Payton");
            meeting1 = new Meeting(name, responsiblePerson1, description, category, type, start, end);
            meeting1.addAttendee(responsiblePerson1);
            meeting1.addAttendee(attendee1);
            meeting1.addAttendee(attendee2);

            Date start2 = new Date(1760787000); //2025-10-18 11:30:00
            Date end2 = new Date(1760790600); //2025-10-18 12:30:00
            String name2 = "Team Java building";
            String description2 = "This is a meeting of building Java";
            Category category2 = Category.TeamBuilding;
            Type type2 = Type.InPerson;
            Person responsiblePerson2 = new Person("ID2", "Dwayne Johnson");
            meeting2 = new Meeting(name2, responsiblePerson2, description2, category2, type2, start2, end2);

            meetings = new MeetingsService();
            meetings.addMeeting(meeting1);
            meetings.addMeeting(meeting2);
        }
        @Test
        void filterByCategoryReturnsMeeting() {
            Meeting[] expectedMeetings = {meeting2};
            MeetingsService filteredMeetings = meetings.filterByCategory("TeamBuilding");
            assertArrayEquals(filteredMeetings.getMeetings().toArray(),  expectedMeetings);
        }
        @Test
        void filterByCategoryReturnsNoMeetings() {
            Meeting[] expectedMeetings = {};
            MeetingsService filteredMeetings = meetings.filterByCategory("CodeMonkey");
            assertArrayEquals(filteredMeetings.getMeetings().toArray(),  expectedMeetings);
        }

        @Test
        void filterByTypeReturnsMeeting() {
            Meeting[] expectedMeetings = {meeting1};
            MeetingsService filteredMeetings = meetings.filterByType("Live");
            assertArrayEquals(filteredMeetings.getMeetings().toArray(),  expectedMeetings);
        }

        @Test
        void filterByDescriptionReturnsMeeting() {
            Meeting[] expectedMeetings = {meeting2};
            MeetingsService filteredMeetings = meetings.filterByDescription("java");
            assertArrayEquals(filteredMeetings.getMeetings().toArray(),  expectedMeetings);
        }

        @Test
        void filterByResponsiblePersonReturnsMeeting() {
            Meeting[] expectedMeetings = {meeting1};
            MeetingsService filteredMeetings = meetings.filterByResponsiblePerson("ID1");
            assertArrayEquals(filteredMeetings.getMeetings().toArray(),  expectedMeetings);
        }

        @Test
        void filterByStartDateReturnsMeeting() {
            Meeting[] expectedMeetings = {meeting2};
            Date filterStart = new Date(1760745600); //2025-10-18
            MeetingsService filteredMeetings = meetings.filterByStartDate(filterStart);
            assertArrayEquals(filteredMeetings.getMeetings().toArray(),  expectedMeetings);
        }

        @Test
        void filterByEndDateReturnsMeeting() {
            Meeting[] expectedMeetings = {meeting2};
            Date filterEnd = new Date(1760832000); //2025-10-18 24:00:00
            MeetingsService filteredMeetings = meetings.filterByEndDate(filterEnd);
            assertArrayEquals(filteredMeetings.getMeetings().toArray(),  expectedMeetings);
        }

        @Test
        void filterByAttendeesCountReturnsMeeting() {
            Meeting[] expectedMeetings = {meeting1};
            MeetingsService filteredMeetings = meetings.filterByAttendeesCount(2);
            assertArrayEquals(filteredMeetings.getMeetings().toArray(),  expectedMeetings);
        }
    }
}