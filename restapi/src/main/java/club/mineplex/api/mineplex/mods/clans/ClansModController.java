package club.mineplex.api.mineplex.mods.clans;

import club.mineplex.api.mineplex.mods.clans.models.Feature;
import club.mineplex.api.mineplex.mods.clans.models.ReleaseInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/clansmod")
@AllArgsConstructor
@Tag(name = "ClansMod")
public class ClansModController {
    private final ClansModService clansModService;
    private final ModRolesService modRolesService;
    private final FeatureService featureService;

    @GetMapping("/version")
    @Operation(summary = "Get the current version")
    public Optional<ReleaseInfo> getVersion() {
        return this.clansModService.getReleaseInfo();
    }

    @GetMapping("/roles")
    @Operation(summary = "Get the roles")
    public Map<String, List<UUID>> getRoles() {
        return this.modRolesService.getModRoles();
    }

    @GetMapping("/features")
    @Operation(summary = "Get all features")
    public List<Feature> verifyStaffFeature() {
        return this.featureService.getFeatures();
    }

    @GetMapping("/download")
    @Operation(summary = "Download the mod")
    public void getDownload(final HttpServletResponse httpServletResponse) {
        final String downloadUrl = this.clansModService.getReleaseInfo()
                                                       .map(ReleaseInfo::getDownloadUrl)
                                                       .filter(Optional::isPresent)
                                                       .map(Optional::get)
                                                       .orElseGet(this.clansModService::getReleaseUrl);

        httpServletResponse.setHeader("Location", downloadUrl);
        httpServletResponse.setStatus(302);
    }
}
