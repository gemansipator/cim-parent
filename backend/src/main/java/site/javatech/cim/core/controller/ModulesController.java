package site.javatech.cim.core.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/modules")
public class ModulesController {

    @GetMapping
    public List<Map<String, Object>> getModules() {
        return Arrays.asList(
                Map.of("id", 1, "name", "Модуль 1", "description", "Описание модуля 1"),
                Map.of("id", 2, "name", "Модуль 2", "description", "Описание модуля 2")
        );
    }
}