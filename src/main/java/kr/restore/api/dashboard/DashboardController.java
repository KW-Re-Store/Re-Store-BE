package kr.restore.api.dashboard;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public DashboardResponse dashboard() {
        return dashboardService.getDashboard();
    }

    @GetMapping("/dashboard/areas")
    public Map<String, Object> areas(@RequestParam Optional<String> grade) {
        Optional<RiskGrade> riskGrade = grade.map(this::parseGrade);

        return Map.of("areas", dashboardService.getRiskAreas(riskGrade));
    }

    @GetMapping("/dashboard/areas/{districtName}")
    public RiskArea area(@PathVariable String districtName) {
        return dashboardService.getRiskAreaByDistrictName(districtName)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "해당 행정동의 상권 위험 데이터를 찾을 수 없습니다."
                ));
    }

    private RiskGrade parseGrade(String grade) {
        try {
            return RiskGrade.fromLabel(grade);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private static class BadRequestException extends RuntimeException {
        BadRequestException(String message) {
            super(message);
        }
    }
}
