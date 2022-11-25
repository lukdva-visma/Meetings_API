package com.example.Meetings_API.Configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class RepositoryConfig {

    private ConfigProperties config;

    @Autowired
    public RepositoryConfig(ConfigProperties config) {
        this.config = config;
    }

    @Bean("MeetingsFile")
    public File meetingFile() {
        return new File(config.getRepositoryPath());
    }
}
