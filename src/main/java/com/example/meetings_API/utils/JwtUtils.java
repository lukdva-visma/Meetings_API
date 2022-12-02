package com.example.meetings_API.utils;

import com.example.meetings_API.configurations.ConfigProperties;
import com.example.meetings_API.exceptions.unauthorized.BadTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final ConfigProperties config;

    private String extractToken(String bearer) {
        String token = "";
        if (bearer.toLowerCase().startsWith("bearer ")) {
            token = bearer.substring(7);
        }
        return token;
    }

    public String getPersonIdFromToken(String bearer) {
        String token = extractToken(bearer);
        try {
            Claims claims = Jwts.parser().setSigningKey(config.getJwtSecret().getBytes()).parseClaimsJws(token).getBody();
            return claims.get("id", String.class);
        } catch (RuntimeException e) {
            throw new BadTokenException(bearer);
        }
    }
}
