package com.example.Meetings_API.Exceptions.unauthorized;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends RuntimeException {
    @Getter
    private final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException() {
    }
}
