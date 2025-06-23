package site.javatech.cim.cimmodel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.javatech.cim.cimmodel.service.BimModelService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * REST-контроллер для управления BIM-моделями в модуле cim-model.
 */
@RestController
@RequestMapping("/api/bim-models")
@Tag(name = "BIM-модели", description = "API для управления BIM-моделями")
public class BimModelController {

    @Autowired
    private BimModelService bimModelService;

    /**
     * Получить список всех BIM-моделей.
     * @return Список моделей в формате Map
     */
    @Operation(summary = "Получить список всех BIM-моделей", description = "Возвращает список всех BIM-моделей.")
    @ApiResponse(responseCode = "200", description = "Список моделей успешно возвращен")
    @GetMapping
    public List<Map<String, Object>> getBimModels() {
        return Arrays.asList(
                Map.of("id", 1, "name", "Модель 1", "filePath", "/models/model1.ifc"),
                Map.of("id", 2, "name", "Модель 2", "filePath", "/models/model2.ifc")
        );
    }
}