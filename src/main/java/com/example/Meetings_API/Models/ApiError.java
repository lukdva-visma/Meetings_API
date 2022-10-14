package com.example.Meetings_API.Models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class ApiError {
    @Getter @Setter
    private String errorMessage;
    @Getter @Setter
    private Date timeStamp;

    public ApiError() {
        timeStamp = new Date();
    }
}
