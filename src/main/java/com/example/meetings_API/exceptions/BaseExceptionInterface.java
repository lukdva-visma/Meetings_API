package com.example.meetings_API.exceptions;

import org.springframework.http.HttpStatus;

public interface BaseExceptionInterface {
    HttpStatus getHttpStatus();

    long getErrorCode();
}
