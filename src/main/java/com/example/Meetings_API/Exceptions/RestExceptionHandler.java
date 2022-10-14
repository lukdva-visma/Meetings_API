package com.example.Meetings_API.Exceptions;

import com.example.Meetings_API.Models.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException e) {
        ApiError error = new ApiError();
        error.setErrorMessage("Not found");
        return new ResponseEntity<ApiError>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<ApiError> handleNotFoundException(UnauthorizedException e) {
        ApiError error = new ApiError();
        error.setErrorMessage("Unauthorized");
        return new ResponseEntity<ApiError>(error, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<NotFoundException> handleException(NotFoundException e) {
        ApiError error = new ApiError();
        error.setErrorMessage(e.getMessage());
        return new ResponseEntity<NotFoundException>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
