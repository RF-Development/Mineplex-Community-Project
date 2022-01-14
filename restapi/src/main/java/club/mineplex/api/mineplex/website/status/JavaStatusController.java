package club.mineplex.api.mineplex.website.status;

import club.mineplex.api.mineplex.website.status.database.JavaTracker;
import club.mineplex.api.mineplex.website.status.models.MetricPeriod;
import club.mineplex.api.mineplex.website.status.models.UptimeMetric;
import club.mineplex.api.mineplex.website.status.models.minecraft.MinecraftServerStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jdbi.v3.core.Jdbi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/status/java")
@Tag(name = "Mineplex")
public class JavaStatusController {

    private final MinecraftStatusService statusService;
    private final Jdbi jdbi;

    public JavaStatusController(final MinecraftStatusService statusService, final Jdbi jdbi) {
        this.statusService = statusService;
        this.jdbi = jdbi;
    }

    @GetMapping("/america")
    @Operation(summary = "Get the online status for Mineplex Java")
    public MinecraftServerStatus getJavaStatus() {
        return this.statusService.getJavaServerStatus();
    }

    @GetMapping("/history")
    @Operation(summary = "Get the daily, weekly, and monthly metrics for Mineplex Java")
    public ResponseEntity<List<List<Long>>> getJavaMetrics(@RequestParam("period") final String span) {
        final Optional<MetricPeriod> periodOpt = MetricPeriod.of(span);
        if (!periodOpt.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }

        final MetricPeriod metricPeriod = periodOpt.get();
        final int period = metricPeriod.getPerodInDays();
        final List<UptimeMetric> metrics =
                this.jdbi.withExtension(JavaTracker.class, tracker -> tracker.getMetrics(period));
        final List<List<Long>> payload = UptimeMetric.asWebsiteChartMetrics(metrics);
        return ResponseEntity.ok(payload);
    }
}
