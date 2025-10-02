package site.javatech.cim.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.javatech.cim.model.Module;
import site.javatech.cim.service.ModuleService;

import java.util.List;

/**
 * REST-контроллер для управления модулями приложения ЦИМ.
 */
@RestController
@RequestMapping("/api/modules")
@Tag(name = "Модули", description = "API для управления модулями приложения ЦИМ")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @Operation(summary = "Получить список всех модулей", description = "Возвращает список всех доступных модулей.")
    @ApiResponse(responseCode = "200", description = "Список модулей успешно возвращен")
    @GetMapping
    public ResponseEntity<List<Module>> getAllModules() {
        return ResponseEntity.ok(moduleService.getAllModules());
    }

    @Operation(summary = "Создать новый модуль", description = "Создает новый модуль с указанными данными.")
    @ApiResponse(responseCode = "200", description = "Модуль успешно создан")
    @PostMapping
    public ResponseEntity<Module> createModule(@RequestBody Module module) {
        return ResponseEntity.ok(moduleService.createModule(module));
    }

    @Operation(summary = "Получить модуль по ID", description = "Возвращает модуль по указанному идентификатору.")
    @ApiResponse(responseCode = "200", description = "Модуль найден")
    @ApiResponse(responseCode = "404", description = "Модуль не найден")
    @GetMapping("/{id}")
    public ResponseEntity<Module> getModuleById(@PathVariable Long id) {
        Module module = moduleService.getModuleById(id);
        if (module == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(module);
    }
}