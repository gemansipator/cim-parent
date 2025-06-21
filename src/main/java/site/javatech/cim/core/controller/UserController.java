package site.javatech.cim.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.javatech.cim.core.model.User;
import site.javatech.cim.core.service.UserService;

import java.util.List;
import java.util.Set;

/**
 * REST-контроллер для управления пользователями приложения ЦИМ.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Пользователи", description = "API для управления пользователями и ролями")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Создать нового пользователя", description = "Создает пользователя с указанными ролями.")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно создан")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user, @RequestParam Set<String> roleNames) {
        return ResponseEntity.ok(userService.createUser(user, roleNames));
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

    @Operation(summary = "Назначить роли пользователю", description = "Назначает указанные роли пользователю (доступно администратору).")
    @ApiResponse(responseCode = "200", description = "Роли успешно назначены")
    @PutMapping("/{id}/roles")
    public ResponseEntity<User> assignRoles(@PathVariable Long id, @RequestParam Set<String> roleNames) {
        return ResponseEntity.ok(userService.assignRoles(id, roleNames));
    }
}