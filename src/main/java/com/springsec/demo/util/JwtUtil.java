package com.springsec.demo.util;

import com.springsec.demo.exception.JwtTokenExpiredException;
import com.springsec.demo.exception.JwtTokenInvalidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate a JWT token
    public String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .claims(claims)
                .subject(email) // Set the subject to the user's email
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    // Validate the token
    public boolean validateToken(String token, String email) {
        try {
            final String username = extractUsername(token);

            // Check if the token belongs to the given email and is not expired
            if (username.equals(email) && !isTokenExpired(token)) {
                return true;
            } else {
                throw new JwtTokenInvalidException("JWT token is invalid");
            }
        } catch (ExpiredJwtException e) {
            throw new JwtTokenExpiredException("JWT token has expired");
        } catch (JwtException e) {
            throw new JwtTokenInvalidException("JWT token invalid");
        }
    }


    // Extract username from the token
    public String extractUsername(String token) {
      return extractAllClaims(token).getSubject();
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // Extract all claims from the token
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
