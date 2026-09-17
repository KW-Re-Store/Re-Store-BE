package kr.restore.api.health;

import java.time.Instant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public ServiceStatus root() {
        return new ServiceStatus("Re-Store API", "running", Instant.now().toString());
    }

    @GetMapping("/health")
    public HealthResponse health() {
        return new HealthResponse("ok", "Re-Store API is running.", Instant.now().toString());
    }

    public record ServiceStatus(String name, String status, String timestamp) {
    }

    public record HealthResponse(String status, String message, String timestamp) {
    }
}
