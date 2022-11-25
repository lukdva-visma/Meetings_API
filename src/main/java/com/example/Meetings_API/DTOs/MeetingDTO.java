package com.example.Meetings_API.DTOs;

import com.example.Meetings_API.Models.Category;
import com.example.Meetings_API.Models.Person;
import com.example.Meetings_API.Models.Type;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MeetingDTO {
    private String name;
    private Person responsiblePerson;
    private String description;
    private Category category;
    private Type type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public MeetingDTO() {
    }
}