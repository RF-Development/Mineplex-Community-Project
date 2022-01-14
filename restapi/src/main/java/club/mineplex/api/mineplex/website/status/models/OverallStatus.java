package club.mineplex.api.mineplex.website.status.models;

import club.mineplex.api.mineplex.website.status.serializers.OverallStatusSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@JsonSerialize(using = OverallStatusSerializer.class)
public class OverallStatus extends ServerStatus {

    private final List<ServerStatus> serverStatuses = new ArrayList<>();

    public OverallStatus(final String name) {
        super(name);

        this.setCondition(Condition.BAD);
    }

    public void updateStatus() {
        final double statusCode = this.serverStatuses.stream()
                .mapToInt(status -> status.getCondition().getCode())
                .average()
                .orElse(0);

        this.setCondition(Arrays.stream(Condition.values())
                .filter(condition -> condition.getCode() == (int) Math.floor(statusCode))
                .findAny()
                .orElse(Condition.BAD));
    }

}
