package club.mineplex.api.mineplex.website.status.models;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UptimeMetric {
    private final Timestamp date;
    private final long ping;

    public static List<List<Long>> asWebsiteChartMetrics(final List<UptimeMetric> metrics) {
        return metrics.stream()
                      .map(metric -> Arrays.asList(metric.getDate().toInstant().toEpochMilli(), metric.getPing()))
                      .collect(Collectors.toList());
    }
}
