package site.javatech.cim.status.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import site.javatech.cim.status.model.Status;
import site.javatech.cim.status.service.StatusService;

/**
 * REST-контроллер для управления статусами в модуле cim-status.
 */
@RestController
@RequestMapping("/api/statuses")
@Tag(name = "Статусы", description = "API для управления статусами")
public class StatusController {

    @Autowired
    private StatusService statusService;

    /**
     * Получить список всех статусов.
     * @return Список имен статусов
     */
    @Operation(summary = "Получить список всех статусов", description = "Возвращает список всех доступных статусов.")
    @ApiResponse(responseCode = "200", description = "Список статусов успешно возвращен")
    @GetMapping
    public ResponseEntity<List<String>> getAllStatuses() {
        List<Status> statuses = statusService.getAllStatuses();
        List<String> statusNames = statuses.stream()
                .map(status -> "Status_" + status.getId()) // Пример имени статуса
                .collect(Collectors.toList());
        return ResponseEntity.ok(statusNames);
    }
}