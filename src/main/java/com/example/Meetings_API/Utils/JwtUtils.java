package com.example.Meetings_API.Utils;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims; import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtils {

    Dotenv dotenv = Dotenv.load();
    private String jwtSecret  = dotenv.get("SECRET");

    private String extractToken( String bearer){
        String token = "";
        if(bearer.toLowerCase().startsWith("bearer "))
            token = bearer.substring(7);
        return token;
    }

    public String getPersonIdFromToken(String bearer)
    {
        String token = extractToken(bearer);
        try{
            Claims claims = Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token).getBody();
            return claims.get("id", String.class);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return "";
    }
}
