package com.example.Meetings_API.Exceptions.badRequest;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException {
    @Getter
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public BadRequestException(String message) {
        super(message);
    }

}
