package com.example.Meetings_API.Exceptions;

import org.springframework.http.HttpStatus;

public interface BaseExceptionInterface {
    HttpStatus getHttpStatus();

    long getErrorCode();
}
