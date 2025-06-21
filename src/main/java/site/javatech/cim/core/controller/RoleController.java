package site.javatech.cim.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.javatech.cim.core.model.Role;
import site.javatech.cim.core.service.RoleService;

import java.util.List;

/**
 * REST-контроллер для управления ролями приложения ЦИМ.
 * Поддерживает только операции чтения ролей.
 */
@RestController
@RequestMapping("/api/roles")
@Tag(name = "Роли", description = "API для просмотра ролей")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Operation(summary = "Получить список всех ролей", description = "Возвращает список всех ролей.")
    @ApiResponse(responseCode = "200", description = "Список ролей успешно возвращен")
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @Operation(summary = "Получить роль по имени", description = "Возвращает роль по указанному имени.")
    @ApiResponse(responseCode = "200", description = "Роль найдена")
    @ApiResponse(responseCode = "404", description = "Роль не найдена")
    @GetMapping("/{name}")
    public ResponseEntity<Role> getRoleByName(@PathVariable String name) {
        Role role = roleService.getRoleByName(name);
        if (role == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(role);
    }
}