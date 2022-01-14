package club.mineplex.api.mineplex.website.status;

import club.mineplex.api.mineplex.website.status.database.BedrockTracker;
import club.mineplex.api.mineplex.website.status.database.JavaTracker;
import club.mineplex.api.mineplex.website.status.models.UptimeMetric;
import lombok.extern.log4j.Log4j2;
import org.jdbi.v3.core.Jdbi;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;

@Service
@Log4j2
public class TrackerService {

    private final MinecraftStatusService statusService;
    private final Jdbi jdbi;

    public TrackerService(final Jdbi jdbi, final MinecraftStatusService statusService) {
        this.jdbi = jdbi;
        this.statusService = statusService;
        jdbi.useExtension(JavaTracker.class, JavaTracker::createTable);
        jdbi.useExtension(BedrockTracker.class, BedrockTracker::createTable);
    }

    @Scheduled(initialDelay = 60_000 * 2, fixedRate = 60_000 * 5)
    private void updateTrackedData() {
        final Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());

        final int currentJavaPing = this.statusService.getJavaServerStatus().getAveragePing();
        final UptimeMetric javaMetric = new UptimeMetric(currentTimestamp, currentJavaPing);
        this.jdbi.useExtension(JavaTracker.class, tracker -> tracker.recordMetric(javaMetric));
        this.jdbi.useExtension(JavaTracker.class, JavaTracker::cleanEntries);

        final int currentEuBedrockPing = this.statusService.getEuBedrockServerStatus().getAveragePing();
        final int currentUsBedrockPing = this.statusService.getUsBedrockServerStatus().getAveragePing();
        final int averageBedrockPing = (currentEuBedrockPing + currentUsBedrockPing) / 2;
        final UptimeMetric bedrockMetric = new UptimeMetric(currentTimestamp, averageBedrockPing);
        this.jdbi.useExtension(BedrockTracker.class, tracker -> tracker.recordMetric(bedrockMetric));
        this.jdbi.useExtension(BedrockTracker.class, BedrockTracker::cleanEntries);
    }

}
