package com.example.Meetings_API.Models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MeetingsFilters {
    private LocalDate start;
    private LocalDate end;
    private String description;
    private String responsiblePersonId;
    private String category;
    private String type;
    private Integer attendees;

    public MeetingsFilters() {
    }
}
