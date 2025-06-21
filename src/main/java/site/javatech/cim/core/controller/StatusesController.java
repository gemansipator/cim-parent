// StatusesController.java
package site.javatech.cim.core.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statuses")
public class StatusesController {

    @GetMapping
    public List<Map<String, Object>> getStatuses() {
        return Arrays.asList(
                Map.of("id", 1, "name", "Статус 1", "description", "Описание статуса 1"),
                Map.of("id", 2, "name", "Статус 2", "description", "Описание статуса 2")
        );
    }
}