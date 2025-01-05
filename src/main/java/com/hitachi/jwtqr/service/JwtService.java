package com.hitachi.jwtqr.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private static final String SECRET_KEY = "your-secret-key91y2b1bsbqnanqjaaayqbq"; // Use a strong secret key
    String base64EncodedKey = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes(StandardCharsets.UTF_8));


    public String generateJwt(long createdAt, long expiresIn, int startingStationNumber, int validStations) {
        String uuid = generateUuid(createdAt, startingStationNumber);

        return Jwts.builder()
                .setSubject("station-info")
                .claim("uuid", uuid)
                .claim("startingStation", startingStationNumber)
                .claim("validStations", validStations)
                .setIssuedAt(new Date(createdAt))
                .setExpiration(new Date(createdAt + expiresIn))
                .signWith(SignatureAlgorithm.HS256, base64EncodedKey)
                .compact();
    }

    private String generateUuid(long createdAt, int startingStationNumber) {
        // Generate UUID based on time and starting station number to ensure uniqueness
        long timePart = createdAt / 1000; // Get seconds for time-based uniqueness
        return UUID.nameUUIDFromBytes((timePart + "-" + startingStationNumber).getBytes()).toString();
    }
}
