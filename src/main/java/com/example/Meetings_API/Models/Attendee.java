package com.example.Meetings_API.Models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class Attendee {
    @Getter @Setter
    private String id;
    @Getter @Setter
    private Person person;
    @Getter @Setter
    private Date added;

    public Attendee() {}
}
