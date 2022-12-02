package com.example.meetings_API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MeetingsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetingsApiApplication.class, args);
    }

}
