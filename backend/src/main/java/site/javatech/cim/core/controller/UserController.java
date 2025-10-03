package site.javatech.cim.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import site.javatech.cim.core.model.Role;
import site.javatech.cim.core.model.User;
import site.javatech.cim.core.security.JwtUtil;
import site.javatech.cim.core.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Контроллер для управления пользователями и аутентификацией.
 * Добавлены эндпоинты для модерации (одобрение, блокировка, разблокировка, удаление, ручное создание).
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
     * Проверка глобальных настроек: если registrationEnabled = false, отклонить.
     * @param userData Данные пользователя (user: {username, password}, roleNames)
     * @return Ответ с информацией о пользователе и JWT-токеном (если одобрен)
     */
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody Map<String, Object> userData) {
        try {
            // Проверка глобальных настроек
            if (!userService.isRegistrationEnabled()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Регистрация временно закрыта администратором"));
            }

            @SuppressWarnings("unchecked")
            Map<String, String> userMap = (Map<String, String>) userData.get("user");
            String username = userMap.get("username");
            String password = userMap.get("password");
            @SuppressWarnings("unchecked")
            List<String> roleNames = (List<String>) userData.get("roleNames");

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
            response.put("message", user.getStatus() == User.Status.PENDING ? "Аккаунт создан. Ожидайте одобрения администратора для входа." : "Регистрация успешна! Вы можете войти.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Ошибка регистрации: " + e.getMessage()));
        }
    }

    /**
     * Аутентификация пользователя.
     * Проверка статуса: PENDING/BLOCKED — отклонить.
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
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage())); // Передаёт сообщение о статусе (ожидание одобрения или блокировка)
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Неверное имя пользователя или пароль"));
        }
    }

    /**
     * Получение списка всех пользователей (для админа).
     * @return Список пользователей
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
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

    /**
     * Одобрение пользователя (для админа).
     * @param id Идентификатор пользователя
     * @return Обновленный пользователь
     */
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> approveUser(@PathVariable Long id) {
        User user = userService.approveUser(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Блокировка пользователя (для админа).
     * @param id Идентификатор пользователя
     * @return Обновленный пользователь
     */
    @PutMapping("/{id}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> blockUser(@PathVariable Long id) {
        User user = userService.blockUser(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Разблокировка пользователя (для админа).
     * @param id Идентификатор пользователя
     * @return Обновленный пользователь
     */
    @PutMapping("/{id}/unblock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> unblockUser(@PathVariable Long id) {
        User user = userService.unblockUser(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Удаление пользователя (для админа).
     * @param id Идентификатор пользователя
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Ручное создание пользователя (для админа).
     * @param userData Данные пользователя (user: {username, password}, roleNames: список ролей)
     * @return Созданный пользователь
     */
    @PostMapping("/manual-create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> manualCreateUser(@RequestBody Map<String, Object> userData) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, String> userMap = (Map<String, String>) userData.get("user");
            User user = new User();
            user.setUsername(userMap.get("username"));
            user.setPassword(userMap.get("password"));
            @SuppressWarnings("unchecked")
            Set<String> roleNames = (Set<String>) userData.get("roleNames");
            user = userService.createUser(user, roleNames);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }
}