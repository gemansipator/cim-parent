package site.javatech.cim.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import site.javatech.cim.core.model.Role;
import site.javatech.cim.core.model.User;
import site.javatech.cim.core.security.JwtUtil;
import site.javatech.cim.core.service.UserService;

import java.util.HashMap;
import java.util.Map;

/**
 * Контроллер для управления пользователями и аутентификацией.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Регистрация нового пользователя.
     * @param userData Данные пользователя (user: {username, password}, roleNames)
     * @return Ответ с информацией о пользователе и JWT-токеном
     */
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody Map<String, Object> userData) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, String> userMap = (Map<String, String>) userData.get("user");
            String username = userMap.get("username");
            if (userService.existsByUsername(username)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Пользователь с именем " + username + " уже существует"));
            }
            User user = userService.createUser(userData);
            String[] roles = user.getRoles().stream().map(Role::getName).toArray(String[]::new);
            String token = jwtUtil.generateToken(user.getUsername(), roles);
            Map<String, Object> response = new HashMap<>();
            response.put("user", user);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Ошибка регистрации: " + e.getMessage()));
        }
    }

    /**
     * Аутентификация пользователя.
     * @param authRequest Данные для входа (username, password)
     * @return Ответ с информацией о пользователе и JWT-токеном
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.get("username"),
                            authRequest.get("password")
                    )
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String[] roles = userService.getRolesByUsername(userDetails.getUsername())
                    .stream().map(Role::getName).toArray(String[]::new);
            String token = jwtUtil.generateToken(userDetails.getUsername(), roles);
            Map<String, Object> response = new HashMap<>();
            response.put("user", userService.getUserByUsername(userDetails.getUsername()));
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Неверное имя пользователя или пароль"));
        }
    }

    /**
     * Получение пользователя по имени.
     * @param username Имя пользователя
     * @return Данные пользователя
     */
    @GetMapping("/by-username")
    public ResponseEntity<?> getUserByUsername(@RequestParam String username) {
        try {
            User user = userService.getUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Пользователь не найден"));
        }
    }
}