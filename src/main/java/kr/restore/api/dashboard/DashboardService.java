package kr.restore.api.dashboard;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.restore.api.dashboard.DashboardResponse.AverageRiskScore;
import kr.restore.api.dashboard.DashboardResponse.CauseSummary;
import kr.restore.api.dashboard.DashboardResponse.ModelInfo;
import kr.restore.api.dashboard.DashboardResponse.PolicyRecommendation;
import kr.restore.api.dashboard.DashboardResponse.SummaryMetric;
import kr.restore.api.dashboard.DashboardResponse.TopRiskArea;
import kr.restore.api.dashboard.RiskArea.ExpectedEffect;

@Service
public class DashboardService {

    private final List<RiskArea> riskAreas = List.of(
            new RiskArea(
                    1,
                    "11350625",
                    "상계10동",
                    RiskGrade.HIGH.label(),
                    82.4,
                    12.4,
                    3.1,
                    -7.6,
                    -9.3,
                    -4.8,
                    1.34,
                    38.2,
                    12.7,
                    "방문객 감소형 상권",
                    "폐업 증가와 함께 매출 및 유동인구가 동반 감소하는 패턴입니다.",
                    List.of("방문객 활성화 지원", "상권 공동마케팅", "관광·문화 연계"),
                    new ExpectedEffect(3.7, 11.6, 11.9, 7.0)
            ),
            new RiskArea(
                    2,
                    "11350624",
                    "상계9동",
                    RiskGrade.HIGH.label(),
                    76.1,
                    11.8,
                    2.7,
                    -6.5,
                    -7.2,
                    -3.9,
                    1.21,
                    41.6,
                    11.5,
                    "과당경쟁형 상권",
                    "동일·유사 업종 밀집도가 높아 경쟁 압력이 큰 패턴입니다.",
                    List.of("업종전환 컨설팅", "창업 사전정보 제공", "특화업종 육성"),
                    new ExpectedEffect(4.7, 9.6, 8.7, 6.7)
            ),
            new RiskArea(
                    3,
                    "11350580",
                    "하계1동",
                    RiskGrade.CAUTION.label(),
                    62.3,
                    10.2,
                    1.4,
                    -4.1,
                    -6.3,
                    -2.4,
                    1.09,
                    33.5,
                    9.8,
                    "방문객 감소형 상권",
                    "매출보다 유동인구 약화가 먼저 나타나는 선제 대응 필요 패턴입니다.",
                    List.of("지역축제", "상권 공동마케팅", "교통 접근성 개선"),
                    new ExpectedEffect(3.1, 10.0, 10.7, 5.9)
            ),
            new RiskArea(
                    4,
                    "11350619",
                    "상계6·7동",
                    RiskGrade.CAUTION.label(),
                    59.2,
                    9.7,
                    1.2,
                    -3.8,
                    -4.7,
                    -2.1,
                    1.03,
                    35.4,
                    9.4,
                    "과당경쟁형 상권",
                    "주요 생활업종의 밀집도가 높아 수익성이 약화되는 패턴입니다.",
                    List.of("업종전환 컨설팅", "입지 분석 제공", "특화업종 육성"),
                    new ExpectedEffect(4.2, 8.2, 7.4, 5.8)
            ),
            new RiskArea(
                    5,
                    "11350570",
                    "월계2동",
                    RiskGrade.CAUTION.label(),
                    56.8,
                    9.2,
                    0.9,
                    -3.4,
                    -4.2,
                    -1.8,
                    0.98,
                    31.3,
                    8.9,
                    "창업 활력 저하형",
                    "신규 창업이 줄고 매출도 약화되는 선제 대응 필요 패턴입니다.",
                    List.of("창업 유치 패키지", "판로 지원", "브랜드 개선"),
                    new ExpectedEffect(2.9, 8.0, 7.3, 5.6)
            ),
            new RiskArea(
                    6,
                    "11350560",
                    "월계1동",
                    RiskGrade.CAUTION.label(),
                    52.6,
                    8.8,
                    0.6,
                    -2.7,
                    -3.5,
                    -1.1,
                    0.93,
                    29.8,
                    8.2,
                    "관찰·관리형 상권",
                    "위험 요인이 일부 있으나 정기 모니터링이 적절한 패턴입니다.",
                    List.of("정기 모니터링", "기초 경영진단", "소상공인 상담 연계"),
                    new ExpectedEffect(2.8, 7.7, 6.9, 5.4)
            )
    );

    public DashboardResponse getDashboard() {
        List<RiskArea> areas = getRiskAreas(Optional.empty());

        return new DashboardResponse(
                new ModelInfo(
                        "Nowon Commercial Area Policy Risk Scoring",
                        "2024 Q1~2025 Q4",
                        "서울특별시 노원구",
                        List.of("점포/개폐업", "추정매출", "유동인구 추정", "공실률 추정"),
                        "초기 백엔드 시드 데이터이며, 원천 CSV 또는 모델 산출물이 연결되면 같은 응답 형식으로 교체할 수 있습니다."
                ),
                buildSummary(areas),
                new AverageRiskScore(round(average(areas.stream().map(RiskArea::riskScore).toList()), 1), -3.5),
                areas.stream()
                        .limit(5)
                        .map(area -> new TopRiskArea(area.rank(), area.districtName(), area.riskGrade(), area.riskScore()))
                        .toList(),
                buildCauses(areas),
                buildIndicators(areas),
                buildPolicyRecommendations(areas),
                areas
        );
    }

    public List<RiskArea> getRiskAreas(Optional<RiskGrade> grade) {
        return riskAreas.stream()
                .filter(area -> grade.map(value -> value.label().equals(area.riskGrade())).orElse(true))
                .sorted(Comparator
                        .comparingInt((RiskArea area) -> RiskGrade.fromLabel(area.riskGrade()).order())
                        .thenComparing(RiskArea::riskScore, Comparator.reverseOrder()))
                .toList();
    }

    public Optional<RiskArea> getRiskAreaByDistrictName(String districtName) {
        String normalizedName = normalize(districtName);

        return riskAreas.stream()
                .filter(area -> normalize(area.districtName()).equals(normalizedName))
                .findFirst();
    }

    private List<SummaryMetric> buildSummary(List<RiskArea> areas) {
        return List.of(
                new SummaryMetric("districts", "전체 행정동 수", 19, "개", "노원구 행정동 기준", "flat"),
                new SummaryMetric("highRiskAreas", "고위험 상권", countByGrade(areas, RiskGrade.HIGH), "개", "전분기 대비 1개 증가", "up"),
                new SummaryMetric("cautionAreas", "주의 상권", countByGrade(areas, RiskGrade.CAUTION), "개", "전분기 대비 변동 없음", "flat"),
                new SummaryMetric("averageRiskScore", "평균 위험지수", round(average(areas.stream().map(RiskArea::riskScore).toList()), 1), "점", "전분기 대비 3.5점 감소", "down"),
                new SummaryMetric("priorityPolicies", "우선 정책 대상", areas.stream().filter(area -> area.riskScore() >= 60).count(), "개", "고위험·주의 상위 지역", "flat")
        );
    }

    private List<CauseSummary> buildCauses(List<RiskArea> areas) {
        return List.of(
                new CauseSummary("유동인구 감소", formatPercent(average(areas.stream().map(RiskArea::footTrafficChangeRate).toList())), "주요 원인 1위"),
                new CauseSummary("매출액 감소", formatPercent(average(areas.stream().map(RiskArea::salesChangeRate).toList())), "주요 원인 2위"),
                new CauseSummary("점포수 감소", formatPercent(average(areas.stream().map(RiskArea::storeChangeRate).toList())), "주요 원인 3위"),
                new CauseSummary("신규 창업 감소", "-8.7%", "지속 감소"),
                new CauseSummary("공실률 증가", "+1.8%p", "지속 증가")
        );
    }

    private List<SummaryMetric> buildIndicators(List<RiskArea> areas) {
        return List.of(
                new SummaryMetric("closureRate", "폐업률", round(average(areas.stream().map(RiskArea::closureRate).toList()), 1), "%", "전분기 대비 3.1% 증가", "up"),
                new SummaryMetric("salesChangeRate", "매출액 증감률", round(average(areas.stream().map(RiskArea::salesChangeRate).toList()), 1), "%", "전분기 대비 3.3% 감소", "down"),
                new SummaryMetric("footTrafficChangeRate", "유동인구 증감률", round(average(areas.stream().map(RiskArea::footTrafficChangeRate).toList()), 1), "%", "전분기 대비 4.2% 감소", "down"),
                new SummaryMetric("vacancyRate", "공실률", round(average(areas.stream().map(RiskArea::vacancyRate).toList()), 1), "%", "전분기 대비 1.9% 증가", "up")
        );
    }

    private List<PolicyRecommendation> buildPolicyRecommendations(List<RiskArea> areas) {
        return areas.stream()
                .limit(3)
                .map(area -> new PolicyRecommendation(
                        area.districtName(),
                        area.causeType(),
                        area.recommendedPolicies(),
                        area.riskScore() >= 60 ? "우선" : "일반",
                        area.expectedEffect().closureRateImprovementPoint(),
                        area.expectedEffect().salesImprovementRate()
                ))
                .toList();
    }

    private long countByGrade(List<RiskArea> areas, RiskGrade grade) {
        return areas.stream().filter(area -> grade.label().equals(area.riskGrade())).count();
    }

    private double average(List<Double> values) {
        if (values.isEmpty()) {
            return 0;
        }

        return values.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    private double round(double value, int digits) {
        double multiplier = Math.pow(10, digits);
        return Math.round(value * multiplier) / multiplier;
    }

    private String formatPercent(double value) {
        return round(value, 1) + "%";
    }

    private String normalize(String value) {
        return value.replaceAll("\\s", "").replace(",", "·");
    }
}
