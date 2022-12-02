package com.example.meetings_API.exceptions.unauthorized;

import com.example.meetings_API.exceptions.BaseExceptionInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BadTokenException extends UnauthorizedException implements BaseExceptionInterface {
    private final String token;
    private final long errorCode = 5L;
}
