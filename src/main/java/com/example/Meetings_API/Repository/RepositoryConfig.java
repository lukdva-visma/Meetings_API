package com.example.Meetings_API.Repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class RepositoryConfig {

    @Bean("MeetingsFile")
    public File meetingFile() {
        return new File("src/main/resources/meetings.json");
    }

    @Bean
    public File personFile() {
        return new File("src/main/resources/person.json");
    }
}
