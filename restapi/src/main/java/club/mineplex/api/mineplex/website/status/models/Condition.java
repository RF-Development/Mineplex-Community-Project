package club.mineplex.api.mineplex.website.status.models;

import club.mineplex.api.mineplex.website.status.models.minecraft.MinecraftRequestInfo;
import club.mineplex.api.mineplex.website.status.serializers.ConditionSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Getter
@JsonSerialize(using = ConditionSerializer.class)
public enum Condition {

    BAD(0, 0.10),
    POOR(1, 0.50),
    OKAY(2, 0.79),
    GOOD(3, 1.00);

    private final int code;
    private final double qualityPercentage;

    Condition(final int code, @DecimalMin("0") @DecimalMax("1") final double qualityPercentage) {
        this.code = code;
        this.qualityPercentage = qualityPercentage;
    }

    public static Condition ofServerRanges(final List<MinecraftRequestInfo> ranges) {
        final int totalRanges = ranges.size();
        final int workingRanges = (int) ranges.stream().filter(MinecraftRequestInfo::isOnline).count();
        final double qualityPercentage = (double) workingRanges / (double) totalRanges;

        return Arrays.stream(Condition.values())
                .filter(condition -> condition.getQualityPercentage() >= qualityPercentage)
                .min(Comparator.comparingDouble(Condition::getQualityPercentage))
                .orElse(Condition.BAD);
    }

}
