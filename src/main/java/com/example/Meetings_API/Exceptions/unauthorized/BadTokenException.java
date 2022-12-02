package com.example.Meetings_API.Exceptions.unauthorized;

import com.example.Meetings_API.Exceptions.BaseExceptionInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BadTokenException extends UnauthorizedException implements BaseExceptionInterface {
    private final String token;
    private final long errorCode = 5L;
}
