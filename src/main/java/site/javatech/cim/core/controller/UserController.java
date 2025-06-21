package site.javatech.cim.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.javatech.cim.core.dto.CreateUserRequest;
import site.javatech.cim.core.model.User;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Получить список всех пользователей.
     * @return Список пользователей
     */
    @Operation(summary = "Получить список всех пользователей", description = "Возвращает список всех пользователей.")
    @ApiResponse(responseCode = "200", description = "Список пользователей успешно возвращен")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Получить пользователя по ID.
     * @param id ID пользователя
     * @return Пользователь или 404, если не найден
     */
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

    /**
     * Получить пользователя по имени.
     * @param username Имя пользователя
     * @return Пользователь или 404, если не найден
     */
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

    /**
     * Создать нового пользователя.
     * @param request Данные пользователя и роли
     * @return Созданный пользователь
     */
    @Operation(summary = "Создать нового пользователя", description = "Создает нового пользователя с указанными ролями.")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно создан")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        if (request.getUser() == null || request.getRoleNames() == null || request.getRoleNames().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        User createdUser = userService.createUser(request.getUser(), request.getRoleNames());
        return ResponseEntity.ok(createdUser);
    }

    /**
     * Назначить роли пользователю.
     * @param id ID пользователя
     * @param roleNames Список имен ролей
     * @return Обновленный пользователь или 404, если не найден
     */
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