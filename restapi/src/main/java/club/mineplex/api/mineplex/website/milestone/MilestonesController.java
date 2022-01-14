package club.mineplex.api.mineplex.website.milestone;

import club.mineplex.api.mineplex.website.milestone.models.Milestones;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Tag(name = "Mineplex")
public class MilestonesController {
    private final MilestonesService milestonesService;

    @GetMapping("/milestones")
    @Operation(summary = "Get all milestones")
    public Milestones getMilestonesFromEpoch(@RequestParam(value = "time", required = false) final Long unixTime) {
        if (unixTime == null) {
            return this.milestonesService.getMilestoneResponse();
        }

        return this.milestonesService.getMilestoneResponse(unixTime);
    }
}
