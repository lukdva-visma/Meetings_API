package com.example.Meetings_API.Models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter @Setter
public class Attendee {
    private String id;
    private Person person;
    private LocalDateTime added;

    public Attendee() {}
    public Attendee(Person person) {
        id = UUID.randomUUID().toString();
        added = LocalDateTime.now();
        this.person = person;
    }
}
