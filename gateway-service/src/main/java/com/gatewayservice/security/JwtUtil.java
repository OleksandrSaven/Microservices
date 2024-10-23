package com.gatewayservice.security;

import com.gatewayservice.exeption.CustomJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private Key secret;

    public JwtUtil(@Value("${jwt.secret}") String secretString) {
        secret = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    public boolean isValidToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            throw new CustomJwtException("Expired or invalid JWT token " + exception);
        }
    }

    public boolean hasAnyRole(String token, List<String> requireRoles) {
        try {
            var parser = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build();
            var claims = parser.parseClaimsJws(token);
            String roles = claims.getBody().get("roles", String.class);
            List<String> roleList = Arrays.asList(roles.split(","));
            return requireRoles.stream().anyMatch(roleList::contains);
        } catch (ExpiredJwtException | SignatureException | MalformedJwtException exception) {
            throw new CustomJwtException("You do not have required permission in the token: "
                    + exception.getMessage());
        }
    }

    public String getUserName(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return claimsJws.getBody().getSubject();
        } catch (JwtException | IllegalArgumentException exception) {
            throw new CustomJwtException("Expired or invalid JWT token " + exception);
        }
    }
}
