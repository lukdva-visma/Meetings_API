package com.example.Meetings_API.Models;

import lombok.Getter;
import lombok.Setter;

public class Person {
    @Getter @Setter
    private String id;
    @Getter @Setter
    private String name;

    public Person() {}
}
