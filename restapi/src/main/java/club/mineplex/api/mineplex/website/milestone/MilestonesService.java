package club.mineplex.api.mineplex.website.milestone;

import club.mineplex.api.mineplex.website.milestone.models.Milestone;
import club.mineplex.api.mineplex.website.milestone.models.Milestones;
import club.mineplex.api.utilities.GoogleUtilities;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
public class MilestonesService {
    private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
    private static final String MILESTONES_DOCUMENT = "1t6TWArU-kcSwk1t4664y8f_Y1zNT9evnCCapSsPV2Ao";

    @Getter
    private final List<Milestone> milestones = new ArrayList<>();
    private LocalDate lastCheck = LocalDate.now();

    @Scheduled(fixedRateString = "PT1H")
    private void updateMilestones() {
        this.milestones.clear();
        final Pair<List<Milestone>, LocalDate> data = this.getMilestones(Instant.now().toEpochMilli() / 1000);
        this.milestones.addAll(data.getFirst());
        this.lastCheck = data.getSecond();
    }

    public Milestones getMilestoneResponse() {
        return new Milestones(this.milestones, this.lastCheck);
    }

    public Milestones getMilestoneResponse(final long epochSeconds) {
        final Pair<List<Milestone>, LocalDate> data = this.getMilestones(epochSeconds);
        return new Milestones(data.getFirst(), data.getSecond());
    }

    private Pair<List<Milestone>, LocalDate> getMilestones(final long unixTime) {
        final List<Milestone> successfulHits = new ArrayList<>();

        final LocalDate currentDate = LocalDate.now(ZoneId.systemDefault());

        final LocalDate requestedDate = Instant.ofEpochSecond(unixTime)
                                               .atZone(ZoneId.systemDefault())
                                               .toLocalDate();
        try {
            final Month currentMonth = requestedDate.getMonth();
            final int currentDay = requestedDate.getDayOfMonth();

            final Spreadsheet sheet = GoogleUtilities.getSheetsService()
                                                     .spreadsheets()
                                                     .get(MILESTONES_DOCUMENT)
                                                     .execute();
            final Sheet milestones = sheet.getSheets().get(0);

            final int rowCount = milestones.getProperties().getGridProperties().getRowCount();

            final List<String> dateColumns = new ArrayList<>();
            for (int i = 2; i < rowCount; i++) {
                dateColumns.addAll(Arrays.asList("A" + i, "B" + i, "E" + i));
            }

            final BatchGetValuesResponse dateResponses = GoogleUtilities.getSheetsService().spreadsheets().
                                                                        values().batchGet(MILESTONES_DOCUMENT)
                                                                        .setRanges(dateColumns).execute();

            for (int i = 0; i < dateResponses.getValueRanges().size(); i += 3) {
                final ValueRange nameValue = dateResponses.getValueRanges().get(i);
                final ValueRange dateValue = dateResponses.getValueRanges().get(i + 1);
                final ValueRange profileValue = dateResponses.getValueRanges().get(i + 2);

                if (nameValue.getValues() == null || dateValue.getValues() == null) {
                    continue;
                }

                String profileURL = null;
                if (profileValue.getValues() != null) {
                    profileURL = (String) profileValue.getValues().get(0).get(0);
                }

                final String name = (String) nameValue.getValues().get(0).get(0);
                final Matcher dayMatcher = DATE_PATTERN.matcher((String) dateValue.getValues().get(0).get(0));

                if (!dayMatcher.find()) {
                    continue;
                }

                final Month month = Month.of(Integer.parseInt(dayMatcher.group(2)));
                final int day = Integer.parseInt(dayMatcher.group(3));
                final int milestone = currentDate.getYear() - Integer.parseInt(dayMatcher.group(1));

                final Milestone ms = new Milestone(milestone, name, profileURL);
                if (day == currentDay && month == currentMonth) {
                    successfulHits.add(ms);
                }

            }

        } catch (final IOException e) {
            log.error(e);
            return Pair.of(new ArrayList<>(), requestedDate);
        }

        return Pair.of(successfulHits, requestedDate);
    }
}
