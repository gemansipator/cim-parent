package site.javatech.cim.requirements.controller;

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
import site.javatech.cim.requirements.model.Requirement;
import site.javatech.cim.requirements.service.RequirementService;

/**
 * REST-контроллер для управления требованиями в модуле cim-requirements.
 */
@RestController
@RequestMapping("/api/requirements")
@Tag(name = "Требования", description = "API для управления требованиями")
public class RequirementController {

    @Autowired
    private RequirementService requirementService;

    /**
     * Получить список всех требований.
     * @return Список имен требований
     */
    @Operation(summary = "Получить список всех требований", description = "Возвращает список всех требований.")
    @ApiResponse(responseCode = "200", description = "Список требований успешно возвращен")
    @GetMapping
    public ResponseEntity<List<String>> getAllRequirements() {
        List<Requirement> requirements = requirementService.getAllRequirements();
        List<String> requirementNames = requirements.stream()
                .map(requirement -> "Requirement_" + requirement.getId()) // Пример имени требования
                .collect(Collectors.toList());
        return ResponseEntity.ok(requirementNames);
    }
}