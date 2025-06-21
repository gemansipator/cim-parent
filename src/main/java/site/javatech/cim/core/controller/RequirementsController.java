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
                Map.of("id", 2, "name", "Требование 2", "description", "Описание требования 2")
        );
    }
}