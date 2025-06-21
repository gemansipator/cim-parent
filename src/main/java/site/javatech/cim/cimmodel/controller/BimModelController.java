package site.javatech.cim.cimmodel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.javatech.cim.cimmodel.model.BimModel;
import site.javatech.cim.cimmodel.service.BimModelService;

import java.util.List;

/**
 * REST-контроллер для управления BIM-моделями в модуле cim-model.
 */
@RestController
@RequestMapping("/api/bim-models")
@Tag(name = "BIM-модели", description = "API для управления BIM-моделями")
public class BimModelController {

    @Autowired
    private BimModelService bimModelService;

    @Operation(summary = "Создать новую BIM-модель", description = "Создает новую BIM-модель.")
    @ApiResponse(responseCode = "200", description = "Модель успешно создана")
    @PostMapping
    public ResponseEntity<BimModel> createBimModel(@RequestBody BimModel bimModel) {
        return ResponseEntity.ok(bimModelService.createBimModel(bimModel));
    }

    @Operation(summary = "Получить список всех BIM-моделей", description = "Возвращает список всех BIM-моделей.")
    @ApiResponse(responseCode = "200", description = "Список моделей успешно возвращен")
    @GetMapping
    public ResponseEntity<List<BimModel>> getAllBimModels() {
        return ResponseEntity.ok(bimModelService.getAllBimModels());
    }

    @Operation(summary = "Получить BIM-модель по ID", description = "Возвращает BIM-модель по указанному ID.")
    @ApiResponse(responseCode = "200", description = "Модель найдена")
    @ApiResponse(responseCode = "404", description = "Модель не найдена")
    @GetMapping("/{id}")
    public ResponseEntity<BimModel> getBimModelById(@PathVariable Long id) {
        BimModel bimModel = bimModelService.getBimModelById(id);
        if (bimModel == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bimModel);
    }
}