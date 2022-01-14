package club.mineplex.api.mineplex.website.status.models;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

public enum MetricPeriod {

    DAY(1), WEEK(7), MONTH(30);

    @Getter
    private final int perodInDays;

    MetricPeriod(final int perodInDays) {
        this.perodInDays = perodInDays;
    }

    public static Optional<MetricPeriod> of(final String name) {
        return Arrays.stream(MetricPeriod.values()).filter(period -> period.name().equalsIgnoreCase(name)).findAny();
    }

}
