package com.example.Meetings_API.Models;

import lombok.Getter;
import lombok.Setter;
@Getter @Setter
public class Person {
    private String id;
    private String name;

    public Person() {}
    public Person(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
