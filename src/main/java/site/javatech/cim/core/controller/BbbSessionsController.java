// BbbSessionsController.java
package site.javatech.cim.core.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bbb-sessions")
public class BbbSessionsController {

    @GetMapping
    public List<Map<String, Object>> getBbbSessions() {
        return Arrays.asList(
                Map.of("id", 1, "name", "Сессия 1", "meetingId", "meeting-1"),
                Map.of("id", 2, "name", "Сессия 2", "meetingId", "meeting-2")
        );
    }
}