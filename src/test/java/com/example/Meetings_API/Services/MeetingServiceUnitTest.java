package com.example.Meetings_API.Services;


import com.example.Meetings_API.Exceptions.NotFoundException;
import com.example.Meetings_API.Models.*;
import com.example.Meetings_API.Repository.MeetingsRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingServiceUnitTest {

    Meeting meeting;
    Person responsiblePerson;
    @Mock
    MeetingsRepository repository;
    @InjectMocks
    MeetingsService meetingsService;

    @BeforeEach
    public void createMeeting() {
        LocalDateTime start = LocalDateTime.of(2025, 10, 17, 10, 0); //2025-10-17 10:00:00
        LocalDateTime end = LocalDateTime.of(2025, 10, 17, 11, 0); //2025-10-17 11:00:00
        String name = "Test team meeting";
        String description = "This is a meeting of test team";
        Category category = Category.Hub;
        Type type = Type.Live;
        responsiblePerson = new Person("id1", "John Jones");
        meeting = new Meeting(name, responsiblePerson, description, category, type, start, end);
    }

    @Test
    @DisplayName("Add meeting method adds meeting to list and calls repository with updated list")
    void verifyAddMeetingMethodAddsMeetingAndCallsRepositoryWithUpdated() {
        Meeting[] expectedMeetingsList = {meeting};
        meetingsService.addMeeting(meeting);
        assertArrayEquals(meetingsService.getFilteredMeetings(new MeetingsFilters()).toArray(), expectedMeetingsList);
        verify(repository).writeMeetings(Arrays.asList(expectedMeetingsList));
    }

    @Test
    @DisplayName("getMeeting returns existing meeting")
    void getExistingMeetingReturnsAddedMeeting() {
        meetingsService.addMeeting(meeting);
        Meeting retrievedMeeting = meetingsService.getMeeting(meeting.getId());
        assertEquals(retrievedMeeting, meeting);
    }

    @Test
    @DisplayName("getMeeting returns null for non existing meeting")
    void tryGettingMeetingNotInTheList() {
        Exception exception = assertThrows(NotFoundException.class, () -> meetingsService.getMeeting(meeting.getId()));
        String expectedMessage = "Meeting not found";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void checkMeetingIsRemovedSuccessfully() {
        meetingsService.addMeeting(meeting);
        reset(repository); // test does not care calls to mock before this statement as it is a setup

        meetingsService.removeMeeting(meeting);
        assertEquals(meetingsService.getFilteredMeetings(new MeetingsFilters()).size(), 0);
        Mockito.verify(repository).writeMeetings(new ArrayList<>());
    }

    @Test
    void listOfMeetingsPersonIsInWhenPersonHasNoMeetings() {
        Meeting[] emptyArray = {};
        meeting.addAttendee(responsiblePerson);
        meetingsService.addMeeting(meeting);
        Person attendee = new Person("id2", "John Smith");
        List<Meeting> listOfMeetings = meetingsService.listOfMeetingsPersonIsIn(attendee);
        assertArrayEquals(listOfMeetings.toArray(), emptyArray);
    }

    @Test
    void listOfMeetingsPersonIsInWhenPersonAttendsOneOfTheMeetings() {
        LocalDateTime date = LocalDateTime.now();
        String name = "Unit Test meeting";
        String description = "This is a meeting of unit tests";
        Category category = Category.CodeMonkey;
        Type type = Type.InPerson;
        Meeting meetingPersonIsIn = new Meeting(name, responsiblePerson, description, category, type, date, date);
        Person attendee = new Person("id2", "John Smith");
        meetingPersonIsIn.addAttendee(attendee);

        Meeting[] meetingArray = {meetingPersonIsIn};
        meeting.addAttendee(responsiblePerson);
        meetingsService.addMeeting(meeting);
        meetingsService.addMeeting(meetingPersonIsIn);

        List<Meeting> listOfMeetings = meetingsService.listOfMeetingsPersonIsIn(attendee);
        assertArrayEquals(listOfMeetings.toArray(), meetingArray);
    }

    @Test
    void personHasConflictingMeetingsReturnsTrue() {
        LocalDateTime start = LocalDateTime.of(2025, 10, 17, 10, 30, 0); //2025-10-17 10:30:00
        LocalDateTime end = LocalDateTime.of(2025, 10, 17, 11, 0, 0); //2025-10-17 11:00:00
        String name = "I Overlap";
        String description = "This definitely overlaps";
        Category category = Category.Short;
        Type type = Type.Live;
        Meeting overlappingMeeting = new Meeting(name, responsiblePerson, description, category, type, start, end);

        Person personWithConflictingMeetings = new Person("id123", "Mr. Conflict");
        meeting.addAttendee(personWithConflictingMeetings);
        meetingsService.addMeeting(meeting);
        meetingsService.addMeeting(overlappingMeeting);
        assertTrue(meetingsService.personHasConflictingMeetings(personWithConflictingMeetings, overlappingMeeting));
    }

    @Test
    void personHasConflictingMeetingsReturnsFalse() {
        LocalDateTime start = LocalDateTime.of(2025, 10, 17, 11, 0, 0); //2025-10-17 11:00:00
        LocalDateTime end = LocalDateTime.of(2025, 10, 17, 12, 0, 0); //2025-10-17 12:00:00
        String name = "I Overlap";
        String description = "This definitely overlaps";
        Category category = Category.Short;
        Type type = Type.Live;
        Meeting nonOverlappingMeeting = new Meeting(name, responsiblePerson, description, category, type, start, end);

        Person personWithoutConflictingMeetings = new Person("id123", "Mr. Conflict");
        meeting.addAttendee(personWithoutConflictingMeetings);
        meetingsService.addMeeting(meeting);
        meetingsService.addMeeting(nonOverlappingMeeting);
        assertFalse(meetingsService.personHasConflictingMeetings(personWithoutConflictingMeetings, nonOverlappingMeeting));
    }

    @Test
    void dateRangesOverlapReturnTrue() {
        LocalDateTime start1 = LocalDateTime.of(2025, 10, 17, 10, 0, 0); //2025-10-17 10:00:00
        LocalDateTime end1 = LocalDateTime.of(2025, 10, 17, 11, 0, 0); //2025-10-17 11:00:00
        Meeting meeting1 = new Meeting();
        meeting1.setStartDate(start1);
        meeting1.setEndDate(end1);

        LocalDateTime start2 = LocalDateTime.of(2025, 10, 17, 10, 30, 0); //2025-10-17 10:30:00
        LocalDateTime end2 = LocalDateTime.of(2025, 10, 17, 12, 0, 0); //2025-10-17 12:00:00
        Meeting meeting2 = new Meeting();
        meeting2.setStartDate(start2);
        meeting2.setEndDate(end2);

        assertTrue(meetingsService.dateRangesOverlap(meeting1, meeting2));
    }

    @Test
    void dateRangesOverlapReturnFalse() {
        LocalDateTime start1 = LocalDateTime.of(2025, 10, 17, 10, 0, 0);  //2025-10-17 10:00:00
        LocalDateTime end1 = LocalDateTime.of(2025, 10, 17, 11, 0, 0);  //2025-10-17 11:00:00
        Meeting meeting1 = new Meeting();
        meeting1.setStartDate(start1);
        meeting1.setEndDate(end1);

        LocalDateTime start2 = LocalDateTime.of(2025, 10, 17, 11, 30, 0);  //2025-10-17 11:30:00
        LocalDateTime end2 = LocalDateTime.of(2025, 10, 17, 12, 0, 0);  //2025-10-17 12:00:00
        Meeting meeting2 = new Meeting();
        meeting2.setStartDate(start2);
        meeting2.setEndDate(end2);

        assertFalse(meetingsService.dateRangesOverlap(meeting1, meeting2));
    }

    @Test
    void dateRangesOverlapReturnFalseWhenBoundariesTouch() {
        LocalDateTime start1 = LocalDateTime.of(2025, 10, 17, 10, 0, 0); //2025-10-17 10:00:00
        LocalDateTime end1 = LocalDateTime.of(2025, 10, 17, 11, 0, 0); //2025-10-17 11:00:00
        Meeting meeting1 = new Meeting();
        meeting1.setStartDate(start1);
        meeting1.setEndDate(end1);

        LocalDateTime start2 = LocalDateTime.of(2025, 10, 17, 11, 0, 0); //2025-10-17 11:00:00
        LocalDateTime end2 = LocalDateTime.of(2025, 10, 17, 12, 0, 0); //2025-10-17 12:00:00
        Meeting meeting2 = new Meeting();
        meeting2.setStartDate(start2);
        meeting2.setEndDate(end2);

        assertFalse(meetingsService.dateRangesOverlap(meeting1, meeting2));
    }

    @Nested
    @DisplayName("Tests for filtering meetings")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @ExtendWith(MockitoExtension.class)
    class TestsFilteringMeetings {
        @Mock
        MeetingsRepository repository;
        @InjectMocks
        MeetingsService meetingsService;
        Meeting meeting1;
        Meeting meeting2;

        @BeforeAll
        public void createMeeting() {
            LocalDateTime start1 = LocalDateTime.of(2025, 10, 17, 10, 0, 0); //2025-10-17 10:00:00
            LocalDateTime end1 = LocalDateTime.of(2025, 10, 19, 11, 30, 0); //2025-10-19 11:30:00
            String name = "Test team meeting";
            String description = "This is a meeting of test team";
            Category category = Category.Hub;
            Type type = Type.Live;
            Person responsiblePerson1 = new Person("ID1", "Michael Carter");
            Person attendee1 = new Person("ID5", "Michael Gary");
            Person attendee2 = new Person("ID6", "Michael Payton");
            meeting1 = new Meeting(name, responsiblePerson1, description, category, type, start1, end1);
            meeting1.addAttendee(responsiblePerson1);
            meeting1.addAttendee(attendee1);
            meeting1.addAttendee(attendee2);

            LocalDateTime start2 = LocalDateTime.of(2025, 10, 18, 11, 30, 0); //2025-10-18 11:30:00
            LocalDateTime end2 = LocalDateTime.of(2025, 10, 18, 12, 30, 0); //2025-10-18 12:30:00
            String name2 = "Team Java building";
            String description2 = "This is a meeting of building Java";
            Category category2 = Category.TeamBuilding;
            Type type2 = Type.InPerson;
            Person responsiblePerson2 = new Person("ID2", "Dwayne Johnson");
            meeting2 = new Meeting(name2, responsiblePerson2, description2, category2, type2, start2, end2);

            MockitoAnnotations.openMocks(this);
            meetingsService.addMeeting(meeting1);
            meetingsService.addMeeting(meeting2);
        }

        @Test
        void filterByCategoryReturnsMeeting() {
            Meeting[] expectedMeetings = {meeting2};
            MeetingsFilters filters = new MeetingsFilters();
            filters.setCategory("TeamBuilding");
            List<Meeting> filteredMeetings = meetingsService.getFilteredMeetings(filters);
            assertArrayEquals(filteredMeetings.toArray(), expectedMeetings);
        }

        @Test
        void filterByCategoryReturnsNoMeetings() {
            Meeting[] expectedMeetings = {};
            MeetingsFilters filters = new MeetingsFilters();
            filters.setCategory("CodeMonkey");
            List<Meeting> filteredMeetings = meetingsService.getFilteredMeetings(filters);
            assertArrayEquals(filteredMeetings.toArray(), expectedMeetings);
        }

        @Test
        void filterByTypeReturnsMeeting() {
            Meeting[] expectedMeetings = {meeting1};
            MeetingsFilters filters = new MeetingsFilters();
            filters.setType("Live");
            List<Meeting> filteredMeetings = meetingsService.getFilteredMeetings(filters);
            assertArrayEquals(filteredMeetings.toArray(), expectedMeetings);
        }

        @Test
        void filterByDescriptionReturnsMeeting() {
            Meeting[] expectedMeetings = {meeting2};
            MeetingsFilters filters = new MeetingsFilters();
            filters.setDescription("java");
            List<Meeting> filteredMeetings = meetingsService.getFilteredMeetings(filters);
            assertArrayEquals(filteredMeetings.toArray(), expectedMeetings);
        }

        @Test
        void filterByResponsiblePersonReturnsMeeting() {
            Meeting[] expectedMeetings = {meeting1};
            MeetingsFilters filters = new MeetingsFilters();
            filters.setResponsiblePersonId("ID1");
            List<Meeting> filteredMeetings = meetingsService.getFilteredMeetings(filters);
            assertArrayEquals(filteredMeetings.toArray(), expectedMeetings);
        }

        @Test
        void filterByStartDateReturnsMeeting() {
            Meeting[] expectedMeetings = {meeting2};
            LocalDate filterStart = LocalDate.of(2025, 10, 18); //2025-10-18
            MeetingsFilters filters = new MeetingsFilters();
            filters.setStart(filterStart);
            List<Meeting> filteredMeetings = meetingsService.getFilteredMeetings(filters);
            assertArrayEquals(filteredMeetings.toArray(), expectedMeetings);
        }

        @Test
        void filterByEndDateReturnsMeeting() {
            Meeting[] expectedMeetings = {meeting2};
            LocalDate filterEnd = LocalDate.of(2025, 10, 18); //2025-10-18
            MeetingsFilters filters = new MeetingsFilters();
            filters.setEnd(filterEnd);
            List<Meeting> filteredMeetings = meetingsService.getFilteredMeetings(filters);
            assertArrayEquals(filteredMeetings.toArray(), expectedMeetings);
        }

        @Test
        void filterByAttendeesCountReturnsMeeting() {
            Meeting[] expectedMeetings = {meeting1};
            MeetingsFilters filters = new MeetingsFilters();
            filters.setAttendees(2);
            List<Meeting> filteredMeetings = meetingsService.getFilteredMeetings(filters);
            assertArrayEquals(filteredMeetings.toArray(), expectedMeetings);
        }
    }
}