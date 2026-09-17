package kr.restore.api.dashboard;

import java.util.List;

public record RiskArea(
        int rank,
        String districtCode,
        String districtName,
        String riskGrade,
        double riskScore,
        double closureRate,
        double closureRateChangePoint,
        double salesChangeRate,
        double footTrafficChangeRate,
        double storeChangeRate,
        double startupClosureRatio,
        double overcrowdingRate,
        double vacancyRate,
        String causeType,
        String causeSummary,
        List<String> recommendedPolicies,
        ExpectedEffect expectedEffect
) {
    public record ExpectedEffect(
            double closureRateImprovementPoint,
            double salesImprovementRate,
            double footTrafficImprovementRate,
            double survivalRateImprovementPoint
    ) {
    }
}
