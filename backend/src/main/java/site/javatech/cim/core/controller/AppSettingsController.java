package site.javatech.cim.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.javatech.cim.core.model.AppSettings;
import site.javatech.cim.core.service.AppSettingsService;

/**
 * Контроллер для управления глобальными настройками (для админа).
 */
@RestController
@RequestMapping("/api/settings")
public class AppSettingsController {

    @Autowired
    private AppSettingsService appSettingsService;

    /**
     * Получить глобальные настройки.
     * @return Настройки
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppSettings> getSettings() {
        return ResponseEntity.ok(appSettingsService.getSettings());
    }

    /**
     * Обновить глобальные настройки.
     * @param settings Настройки
     * @return Обновлённые настройки
     */
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppSettings> updateSettings(@RequestBody AppSettings settings) {
        return ResponseEntity.ok(appSettingsService.updateSettings(settings));
    }
}