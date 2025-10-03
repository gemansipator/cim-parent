package site.javatech.cim.bbb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.javatech.cim.bbb.service.BbbSessionService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * REST-контроллер для управления сессиями BigBlueButton в модуле bbb.
 */
@RestController
@RequestMapping("/api/bbb-sessions")
@Tag(name = "BBB Сессии", description = "API для управления сессиями BigBlueButton")
public class BbbSessionController {

    @Autowired
    private BbbSessionService bbbSessionService;

    /**
     * Получить список всех сессий BBB.
     * @return Список сессий в формате Map
     */
    @Operation(summary = "Получить список всех сессий BBB", description = "Возвращает список всех сессий BBB.")
    @ApiResponse(responseCode = "200", description = "Список сессий успешно возвращен")
    @GetMapping
    public List<Map<String, Object>> getBbbSessions() {
        return Arrays.asList(
                Map.of("id", 1, "name", "Сессия 1", "meetingId", "meeting-1"),
                Map.of("id", 2, "name", "Сессия 2", "meetingId", "meeting-2")
        );
    }
}