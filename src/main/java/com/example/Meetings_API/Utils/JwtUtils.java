package com.example.Meetings_API.Utils;

import com.example.Meetings_API.Exceptions.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    @Value("${SECRET}")
    private String jwtSecret;

    private String extractToken(String bearer) {
        String token = "";
        if (bearer.toLowerCase().startsWith("bearer "))
            token = bearer.substring(7);
        return token;
    }

    public String getPersonIdFromToken(String bearer) {
        String token = extractToken(bearer);
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token).getBody();
            return claims.get("id", String.class);
        } catch (RuntimeException e) {
            throw new UnauthorizedException("Unable to parse information from token");
        }
    }
}
