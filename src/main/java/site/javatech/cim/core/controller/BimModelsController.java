// BimModelsController.java
package site.javatech.cim.core.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bim-models")
public class BimModelsController {

    @GetMapping
    public List<Map<String, Object>> getBimModels() {
        return Arrays.asList(
                Map.of("id", 1, "name", "Модель 1", "filePath", "/models/model1.ifc"),
                Map.of("id", 2, "name", "Модель 2", "filePath", "/models/model2.ifc")
        );
    }
}