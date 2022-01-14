package club.mineplex.api.mineplex.website.milestone.models;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Milestones {
    private final List<Milestone> milestones;
    private final LocalDate dataDate;
}
