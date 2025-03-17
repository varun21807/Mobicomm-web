package com.mobicomm.app.security;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private Key signingKey;

    // Expiration times
    private static final long ACCESS_EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 hours
    private static final long REFRESH_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; // 7 days

    @PostConstruct
    public void init() {
        if (secretKey.length() < 32) {
            throw new IllegalArgumentException("JWT secret key must be at least 32 characters long.");
        }
        signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // ✅ Generate Access Token (Admin uses email, User uses phone number)
    public String generateAccessToken(String identifier, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)  // Add roles or extra data
                .setSubject(identifier)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_TIME))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Generate Refresh Token (Admin uses email, User uses phone number)
    public String generateRefreshToken(String identifier) {
        return Jwts.builder()
                .setSubject(identifier)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Extract Identifier (Email for Admin, Phone for User)
    public String extractIdentifier(String token) {
        return extractClaims(token).getSubject();
    }

    // ✅ Extract Phone Number from JWT
    public String extractPhoneNumber(String token) {
        Claims claims = extractClaims(token);
        return claims.get("phoneNumber", String.class);
    }

    // ✅ Extract Custom Claims (e.g., roles)
    public Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expired. Please log in again.");
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Invalid token format.");
        } catch (SignatureException e) {
            throw new RuntimeException("Invalid token signature.");
        } catch (JwtException e) {
            throw new RuntimeException("Token processing error.");
        }
    }

    // ✅ Validate Token (Check Expiration & Signature)
    public boolean validateToken(String token) {
        try {
            extractClaims(token); // Calls method that already handles exceptions
            return true;
        } catch (Exception e) {
            System.out.println("JWT Validation Error: " + e.getMessage());
            return false;
        }
    }
}
