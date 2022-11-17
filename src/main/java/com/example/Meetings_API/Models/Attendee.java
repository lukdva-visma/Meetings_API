package com.example.Meetings_API.Models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

public class Attendee {
    @Getter @Setter
    private String id;
    @Getter @Setter
    private Person person;
    @Getter @Setter
    private LocalDateTime added;

    public Attendee() {}
    public Attendee(Person person) {
        id = UUID.randomUUID().toString();
        added = LocalDateTime.now();
        this.person = person;
    }
}
