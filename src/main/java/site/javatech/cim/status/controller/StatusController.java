package site.javatech.cim.status.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.javatech.cim.status.service.StatusService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
     * @return Список статусов в формате Map
     */
    @Operation(summary = "Получить список всех статусов", description = "Возвращает список всех доступных статусов.")
    @ApiResponse(responseCode = "200", description = "Список статусов успешно возвращен")
    @GetMapping
    public List<Map<String, Object>> getStatuses() {
        return Arrays.asList(
                Map.of("id", 1, "name", "Статус 1", "description", "Описание статуса 1"),
                Map.of("id", 2, "name", "Статус 2", "description", "Описание статуса 2")
        );
    }
}