package br.com.fiap.wtccrm.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utilitário para criar e validar tokens JWT (HS256). Claims principais: {@code sub} = userId,
 * {@code role} = OPERATOR ou CLIENT. Expiração configurável via {@code jwt.expiration}.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:28800000}")
    private long expiration;

    /** Gera JWT assinado com a chave configurada em {@code jwt.secret}. */
    public String generateToken(String userId, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .subject(userId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /** Retorna true se assinatura e expiração forem válidas. */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /** Extrai o subject do token (identificador do usuário). */
    public String extractUserId(String token) {
        return getClaims(token).getSubject();
    }

    /** Extrai o claim {@code role} (sem prefixo ROLE_). */
    public String extractRole(String token) {
        Object role = getClaims(token).get("role");
        return role == null ? "" : role.toString();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes.length >= 32 ? bytes : String.format("%-32s", secret).getBytes(StandardCharsets.UTF_8));
    }
}
