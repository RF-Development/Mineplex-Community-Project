package club.mineplex.api.mineplex.website.profile;

import club.mineplex.api.mineplex.website.profile.models.EntireProfile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
@Tag(name = "Mineplex")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/forum/user/{memberId}")
    @Operation(summary = "Get the forum user")
    public Optional<EntireProfile> getForumUser(@PathVariable final int memberId) {
        return this.profileService.getEntireProfile(memberId);
    }
}
