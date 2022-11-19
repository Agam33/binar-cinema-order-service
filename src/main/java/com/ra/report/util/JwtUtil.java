package com.ra.report.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${app.jwt.key}")
    private String jwtKey;

    @SuppressWarnings("deprecation")
    public String getUserNameFromJwtToken(String token) {
        return Jwts
                .parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody().getSubject();
    }

    @SuppressWarnings("deprecation")
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            LOGGER.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JWT toke: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("JWT token is expired {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JWT claims string is empty {}", e.getMessage());
        }
        return false;
    }
}
