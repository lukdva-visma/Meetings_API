package com.example.meetings_API.dto;

import com.example.meetings_API.models.Attendee;
import com.example.meetings_API.models.Category;
import com.example.meetings_API.models.Type;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
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
    
}
