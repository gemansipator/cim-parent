package site.javatech.cim.requirements.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.javatech.cim.requirements.service.RequirementService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
     * @return Список требований в формате Map
     */
    @Operation(summary = "Получить список всех требований", description = "Возвращает список всех требований.")
    @ApiResponse(responseCode = "200", description = "Список требований успешно возвращен")
    @GetMapping
    public List<Map<String, Object>> getRequirements() {
        return Arrays.asList(
                Map.of("id", 1, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 2, "name", "Требование 2", "description", "Описание требования 2")
        );
    }
}