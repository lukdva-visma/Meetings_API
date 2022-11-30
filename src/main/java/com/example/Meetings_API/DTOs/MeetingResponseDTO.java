package com.example.Meetings_API.DTOs;

import com.example.Meetings_API.Models.Attendee;
import com.example.Meetings_API.Models.Category;
import com.example.Meetings_API.Models.Type;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MeetingResponseDTO {
    private String id;
    private String name;
    private PersonDTO responsiblePerson;
    private String description;
    private Category category;
    private Type type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Attendee> attendees = new ArrayList<>();

    public MeetingResponseDTO() {
    }

    public MeetingResponseDTO(String id, String name, PersonDTO responsiblePerson, String description, Category category, Type type, LocalDateTime startDate, LocalDateTime endDate, List<Attendee> attendees) {
        this.id = id;
        this.name = name;
        this.responsiblePerson = responsiblePerson;
        this.description = description;
        this.category = category;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.attendees = attendees;
    }
}
