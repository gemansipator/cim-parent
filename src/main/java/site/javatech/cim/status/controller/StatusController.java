package site.javatech.cim.status.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.javatech.cim.status.model.Status;
import site.javatech.cim.status.service.StatusService;

import java.util.List;

/**
 * REST-контроллер для управления статусами в модуле cim-status.
 */
@RestController
@RequestMapping("/api/statuses")
@Tag(name = "Статусы", description = "API для управления статусами")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @Operation(summary = "Создать новый статус", description = "Создает новый статус.")
    @ApiResponse(responseCode = "200", description = "Статус успешно создан")
    @PostMapping
    public ResponseEntity<Status> createStatus(@RequestBody Status status) {
        return ResponseEntity.ok(statusService.createStatus(status));
    }

    @Operation(summary = "Получить список всех статусов", description = "Возвращает список всех статусов.")
    @ApiResponse(responseCode = "200", description = "Список статусов успешно возвращен")
    @GetMapping
    public ResponseEntity<List<Status>> getAllStatuses() {
        return ResponseEntity.ok(statusService.getAllStatuses());
    }

    @Operation(summary = "Получить статус по ID", description = "Возвращает статус по указанному ID.")
    @ApiResponse(responseCode = "200", description = "Статус найден")
    @ApiResponse(responseCode = "404", description = "Статус не найден")
    @GetMapping("/{id}")
    public ResponseEntity<Status> getStatusById(@PathVariable Long id) {
        Status status = statusService.getStatusById(id);
        if (status == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(status);
    }
}