package site.javatech.cim.bbb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.javatech.cim.bbb.model.BbbSession;
import site.javatech.cim.bbb.service.BbbSessionService;

import java.util.List;

/**
 * REST-контроллер для управления сессиями BigBlueButton в модуле bbb.
 */
@RestController
@RequestMapping("/api/bbb-sessions")
@Tag(name = "BBB Сессии", description = "API для управления сессиями BigBlueButton")
public class BbbSessionController {

    @Autowired
    private BbbSessionService bbbSessionService;

    @Operation(summary = "Создать новую сессию BBB", description = "Создает новую сессию BigBlueButton.")
    @ApiResponse(responseCode = "200", description = "Сессия успешно создана")
    @PostMapping
    public ResponseEntity<BbbSession> createBbbSession(@RequestBody BbbSession bbbSession) {
        return ResponseEntity.ok(bbbSessionService.createBbbSession(bbbSession));
    }

    @Operation(summary = "Получить список всех сессий BBB", description = "Возвращает список всех сессий BBB.")
    @ApiResponse(responseCode = "200", description = "Список сессий успешно возвращен")
    @GetMapping
    public ResponseEntity<List<BbbSession>> getAllBbbSessions() {
        return ResponseEntity.ok(bbbSessionService.getAllBbbSessions());
    }

    @Operation(summary = "Получить сессию BBB по ID", description = "Возвращает сессию BBB по указанному ID.")
    @ApiResponse(responseCode = "200", description = "Сессия найдена")
    @ApiResponse(responseCode = "404", description = "Сессия не найдена")
    @GetMapping("/{id}")
    public ResponseEntity<BbbSession> getBbbSessionById(@PathVariable Long id) {
        BbbSession bbbSession = bbbSessionService.getBbbSessionById(id);
        if (bbbSession == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bbbSession);
    }
}