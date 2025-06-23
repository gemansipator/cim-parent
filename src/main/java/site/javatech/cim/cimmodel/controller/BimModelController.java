package site.javatech.cim.cimmodel.controller;

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
import site.javatech.cim.cimmodel.model.BimModel;
import site.javatech.cim.cimmodel.service.BimModelService;

/**
 * REST-контроллер для управления BIM-моделями в модуле cim-model
 */
@RestController
@RequestMapping("/api/bim-models")
@Tag(name = "BIM-модели", description = "API для управления BIM-моделями")
public class BimModelController {

    @Autowired
    private BimModelService bimModelService;

    /**
     * Получить список всех BIM-моделей.
     * @return Список имен моделей
     */
    @Operation(summary = "Получить список всех BIM-моделей", description = "Возвращает список всех BIM-моделей.")
    @ApiResponse(responseCode = "200", description = "Список моделей успешно возвращен")
    @GetMapping
    public ResponseEntity<List<String>> getAllBimModels() {
        List<BimModel> bimModels = bimModelService.getAllBimModels();
        List<String> modelNames = bimModels.stream()
                .map(model -> "BIM_Model_" + model.getId()) // Пример имени модели
                .collect(Collectors.toList());
        return ResponseEntity.ok(modelNames);
    }
}