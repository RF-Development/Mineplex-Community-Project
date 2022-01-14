package club.mineplex.api.mineplex.website.status;

import club.mineplex.api.mineplex.website.status.models.OverallStatus;
import club.mineplex.api.mineplex.website.status.models.website.HttpSiteStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/status")
@Tag(name = "Mineplex")
public class CommonStatusController {

    private final CommonStatusService httpStatusService;
    private final OverallStatus overallStatus;

    public CommonStatusController(final CommonStatusService httpStatusService, final MinecraftStatusService mcService) {
        this.httpStatusService = httpStatusService;
        this.overallStatus = this.httpStatusService.getOverallStatus();
        this.overallStatus.getServerStatuses().addAll(Arrays.asList(
                httpStatusService.getStoreStatus(),
                httpStatusService.getWebsiteStatus(),
                mcService.getJavaServerStatus(),
                mcService.getEuBedrockServerStatus(),
                mcService.getUsBedrockServerStatus()
        ));
    }

    @GetMapping
    @Operation(summary = "Get the overall status of the Mineplex Network")
    public OverallStatus getOverallStatus() {
        return this.overallStatus;
    }

    @GetMapping("/website")
    @Operation(summary = "Get the status of the Mineplex website")
    public HttpSiteStatus getWebsiteStatus() {
        return this.httpStatusService.getWebsiteStatus();
    }

    @GetMapping("/store")
    @Operation(summary = "Get the status of the Mineplex store")
    public HttpSiteStatus getStoreStatus() {
        return this.httpStatusService.getStoreStatus();
    }
}
