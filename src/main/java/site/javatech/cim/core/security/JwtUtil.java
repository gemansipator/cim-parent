package site.javatech.cim.core.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

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
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
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
                .setSubject(username)
                .claim("roles", String.join(",", roles))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
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