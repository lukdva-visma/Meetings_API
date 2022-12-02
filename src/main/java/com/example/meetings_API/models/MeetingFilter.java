package com.example.meetings_API.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MeetingFilter {
    private LocalDate start;
    private LocalDate end;
    private String description;
    private String responsiblePersonId;
    private String category;
    private String type;
    private Integer attendees;

    public MeetingFilter() {
    }
}
