package com.example.Meetings_API.Exceptions;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
    public BadRequestException() {

    }
}
