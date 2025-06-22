// RequirementsController.java
package site.javatech.cim.core.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/requirements")
public class RequirementsController {

    @GetMapping
    public List<Map<String, Object>> getRequirements() {
        return Arrays.asList(
                Map.of("id", 1, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 2, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 3, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 4, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 5, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 6, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 7, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 8, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 9, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 10, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 11, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 12, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 13, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 14, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 15, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 16, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 17, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 18, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 19, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 20, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 21, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 22, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 23, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 24, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 25, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 26, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 27, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 28, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 29, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 30, "name", "Требование 1", "description", "Описание требования 1"),
                Map.of("id", 2, "name", "Требование 2", "description", "Описание требования 2")
        );
    }
}