package kr.restore.api.dashboard;

import java.util.Arrays;

public enum RiskGrade {
    HIGH("고위험", 0),
    CAUTION("주의", 1),
    WATCH("관심", 2);

    private final String label;
    private final int order;

    RiskGrade(String label, int order) {
        this.label = label;
        this.order = order;
    }

    public String label() {
        return label;
    }

    public int order() {
        return order;
    }

    public static RiskGrade fromLabel(String label) {
        return Arrays.stream(values())
                .filter(grade -> grade.label.equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("grade는 고위험, 주의, 관심 중 하나여야 합니다."));
    }
}
