package site.javatech.cim.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST-контроллер для управления настройками и модерацией.
 */
@RestController
@RequestMapping("/api/settings-moderation")
@Tag(name = "Настройки и модерация", description = "API для управления настройками и модерацией")
public class SettingsModerationController {

    @Operation(summary = "Получить данные настроек и модерации", description = "Возвращает данные для модуля настроек и модерации. Доступно только для ADMIN и SUPERUSER.")
    @ApiResponse(responseCode = "200", description = "Данные успешно возвращены")
    @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERUSER')")
    public ResponseEntity<Map<String, String>> getSettingsModeration() {
        return ResponseEntity.ok(Map.of("message", "Настройки и модерация доступны для ADMIN и SUPERUSER"));
    }
}