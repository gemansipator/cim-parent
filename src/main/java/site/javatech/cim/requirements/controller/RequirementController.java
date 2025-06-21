package site.javatech.cim.requirements.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.javatech.cim.requirements.model.Requirement;
import site.javatech.cim.requirements.service.RequirementService;

import java.util.List;

/**
 * REST-контроллер для управления требованиями в модуле cim-requirements.
 */
@RestController
@RequestMapping("/api/requirements")
@Tag(name = "Требования", description = "API для управления требованиями")
public class RequirementController {

    @Autowired
    private RequirementService requirementService;

    @Operation(summary = "Создать новое требование", description = "Создает новое требование.")
    @ApiResponse(responseCode = "200", description = "Требование успешно создано")
    @PostMapping
    public ResponseEntity<Requirement> createRequirement(@RequestBody Requirement requirement) {
        return ResponseEntity.ok(requirementService.createRequirement(requirement));
    }

    @Operation(summary = "Получить список всех требований", description = "Возвращает список всех требований.")
    @ApiResponse(responseCode = "200", description = "Список требований успешно возвращен")
    @GetMapping
    public ResponseEntity<List<Requirement>> getAllRequirements() {
        return ResponseEntity.ok(requirementService.getAllRequirements());
    }

    @Operation(summary = "Получить требование по ID", description = "Возвращает требование по указанному ID.")
    @ApiResponse(responseCode = "200", description = "Требование найдено")
    @ApiResponse(responseCode = "404", description = "Требование не найдено")
    @GetMapping("/{id}")
    public ResponseEntity<Requirement> getRequirementById(@PathVariable Long id) {
        Requirement requirement = requirementService.getRequirementById(id);
        if (requirement == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(requirement);
    }
}