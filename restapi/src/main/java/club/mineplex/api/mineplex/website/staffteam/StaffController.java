package club.mineplex.api.mineplex.website.staffteam;

import club.mineplex.api.mineplex.website.profile.models.Profile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@Tag(name = "Mineplex")
public class StaffController {
    private final StaffService staffService;

    @GetMapping("/stafflist")
    @Operation(summary = "Get the staff team")
    public Map<String, List<Profile>> getStaffList() {
        return this.staffService.getStaffMembers();
    }
}
