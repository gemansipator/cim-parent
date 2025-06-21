package site.javatech.cim.bbb.controller;

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
import site.javatech.cim.bbb.model.BbbSession;
import site.javatech.cim.bbb.service.BbbSessionService;

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
     * @return Список имен сессий
     */
    @Operation(summary = "Получить список всех сессий BBB", description = "Возвращает список всех сессий BBB.")
    @ApiResponse(responseCode = "200", description = "Список сессий успешно возвращен")
    @GetMapping
    public ResponseEntity<List<String>> getAllBbbSessions() {
        List<BbbSession> sessions = bbbSessionService.getAllBbbSessions();
        List<String> sessionNames = sessions.stream()
                .map(session -> "Session_" + session.getId()) // Пример имени сессии
                .collect(Collectors.toList());
        return ResponseEntity.ok(sessionNames);
    }
}