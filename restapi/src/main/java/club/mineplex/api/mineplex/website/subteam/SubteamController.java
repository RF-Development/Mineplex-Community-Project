package club.mineplex.api.mineplex.website.subteam;

import club.mineplex.api.mineplex.website.subteam.models.Subteam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Mineplex")
public class SubteamController {
    private final SubteamService subteamService;

    @GetMapping("/subteams")
    @Operation(summary = "Get the subteams")
    public List<Subteam> getSubteams() {
        return this.subteamService.getSubteams();
    }
}
