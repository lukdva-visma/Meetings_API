package com.example.Meetings_API.Models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter @Setter
public class ApiError {
    private String errorMessage;
    private Date timeStamp;

    public ApiError() {
        timeStamp = new Date();
    }
}
