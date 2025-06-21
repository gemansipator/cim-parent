package site.javatech.cim.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import site.javatech.cim.core.dto.CreateUserRequest;
import site.javatech.cim.core.dto.LoginRequest;
import site.javatech.cim.core.dto.LoginResponse;
import site.javatech.cim.core.model.User;
import site.javatech.cim.core.security.JwtUtil;
import site.javatech.cim.core.service.UserService;

import java.util.List;
import java.util.Set;

/**
 * Контроллер для управления пользователями и их ролями.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Пользователи", description = "API для управления пользователями и ролями")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Получить список всех пользователей", description = "Возвращает список всех пользователей.")
    @ApiResponse(responseCode = "200", description = "Список пользователей успешно возвращен")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя по указанному ID.")
    @ApiResponse(responseCode = "200", description = "Пользователь найден")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Получить пользователя по имени", description = "Возвращает пользователя по имени пользователя.")
    @ApiResponse(responseCode = "200", description = "Пользователь найден")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @GetMapping("/by-username")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Создать нового пользователя", description = "Создает нового пользователя с указанными ролями.")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно создан")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> createUser(@RequestBody CreateUserRequest request) {
        if (request.getUser() == null || request.getRoleNames() == null || request.getRoleNames().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        User user = request.getUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser = userService.createUser(user, request.getRoleNames());
        String[] roles = createdUser.getRoles().stream().map(Role::getName).toArray(String[]::new);
        String token = jwtUtil.generateToken(createdUser.getUsername(), roles);
        return ResponseEntity.ok(new LoginResponse(createdUser, token));
    }

    @Operation(summary = "Аутентификация пользователя", description = "Аутентифицирует пользователя и возвращает JWT.")
    @ApiResponse(responseCode = "200", description = "Аутентификация успешна")
    @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userService.getUserByUsername(request.getUsername());
        String[] roles = user.getRoles().stream().map(Role::getName).toArray(String[]::new);
        String token = jwtUtil.generateToken(user.getUsername(), roles);
        return ResponseEntity.ok(new LoginResponse(user, token));
    }

    @Operation(summary = "Назначить роли пользователю", description = "Назначает указанные роли пользователю по ID.")
    @ApiResponse(responseCode = "200", description = "Роли успешно назначены")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @PutMapping("/{id}/roles")
    public ResponseEntity<User> assignRoles(@PathVariable Long id, @RequestParam Set<String> roleNames) {
        User user = userService.assignRoles(id, roleNames);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
}