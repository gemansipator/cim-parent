package site.javatech.cim.core.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Утилита для работы с JWT-токенами.
 * Отвечает за создание, проверку и извлечение данных из токенов.
 */
@Component
public class JwtUtil {

    private final String SECRET_KEY = "7x/A3DqZqF3N8JkYh+I2l4v5mQ6bP9Er0cL1tUuVwXyZ";
    private final long EXPIRATION_TIME = 86400000; // 24 часа
    private final SecretKey signingKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    /**
     * Извлечь имя пользователя из токена.
     * @param token JWT-токен
     * @return Имя пользователя
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Извлечь роли из токена.
     * @param token JWT-токен
     * @return Массив ролей
     */
    public String[] extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", String.class).split(",");
    }

    /**
     * Извлечь конкретное утверждение из токена.
     * @param token JWT-токен
     * @param claimsResolver Функция для извлечения утверждения
     * @param <T> Тип утверждения
     * @return Значение утверждения
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Извлечь все утверждения из токена.
     * @param token JWT-токен
     * @return Утверждения
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Проверить, истёк ли токен.
     * @param token JWT-токен
     * @return true, если токен истёк
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Извлечь дату истечения токена.
     * @param token JWT-токен
     * @return Дата истечения
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Сгенерировать JWT-токен.
     * @param username Имя пользователя
     * @param roles Роли пользователя
     * @return JWT-токен
     */
    public String generateToken(String username, String[] roles) {
        return Jwts.builder()
                .subject(username)
                .claim("roles", String.join(",", roles))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(signingKey)
                .compact();
    }

    /**
     * Проверить токен на валидность.
     * @param token JWT-токен
     * @param username Имя пользователя
     * @return true, если токен валиден
     */
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}