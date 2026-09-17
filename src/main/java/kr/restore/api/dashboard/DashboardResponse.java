package kr.restore.api.dashboard;

import java.util.List;

public record DashboardResponse(
        ModelInfo model,
        List<SummaryMetric> summary,
        AverageRiskScore averageRiskScore,
        List<TopRiskArea> topRiskAreas,
        List<CauseSummary> causes,
        List<SummaryMetric> indicators,
        List<PolicyRecommendation> policyRecommendations,
        List<RiskArea> areas
) {
    public record ModelInfo(
            String name,
            String period,
            String area,
            List<String> dataSource,
            String note
    ) {
    }

    public record SummaryMetric(
            String key,
            String label,
            double value,
            String unit,
            String note,
            String trend
    ) {
    }

    public record AverageRiskScore(double value, double changePoint) {
    }

    public record TopRiskArea(int rank, String districtName, String riskGrade, double riskScore) {
    }

    public record CauseSummary(String label, String value, String tag) {
    }

    public record PolicyRecommendation(
            String districtName,
            String causeType,
            List<String> policies,
            String priority,
            double expectedClosureRateImprovementPoint,
            double expectedSalesImprovementRate
    ) {
    }
}
