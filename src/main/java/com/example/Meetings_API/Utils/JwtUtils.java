package com.example.Meetings_API.Utils;

import com.example.Meetings_API.Configurations.ConfigProperties;
import com.example.Meetings_API.Exceptions.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
    
    private ConfigProperties config;

    public JwtUtils(ConfigProperties config) {
        this.config = config;
    }

    private String extractToken(String bearer) {
        String token = "";
        if (bearer.toLowerCase().startsWith("bearer "))
            token = bearer.substring(7);
        return token;
    }

    public String getPersonIdFromToken(String bearer) {
        String token = extractToken(bearer);
        try {
            Claims claims = Jwts.parser().setSigningKey(config.getJwtSecret().getBytes()).parseClaimsJws(token).getBody();
            return claims.get("id", String.class);
        } catch (RuntimeException e) {
            throw new UnauthorizedException("Unable to parse information from token");
        }
    }
}
