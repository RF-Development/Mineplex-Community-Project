package club.mineplex.api.mineplex.website.status;

import club.mineplex.api.mineplex.website.status.models.Condition;
import club.mineplex.api.mineplex.website.status.models.OverallStatus;
import club.mineplex.api.mineplex.website.status.models.website.HttpSiteStatus;
import club.mineplex.api.utilities.HttpClientUtilities;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Log4j2
public class CommonStatusService {

    @Getter
    private final HttpSiteStatus websiteStatus = new HttpSiteStatus("website", "https://www.mineplex.com/");

    @Getter
    private final HttpSiteStatus storeStatus = new HttpSiteStatus("store", "https://shop.mineplex.com/checkout/basket");

    @Getter
    private final OverallStatus overallStatus = new OverallStatus("overall");

    @Scheduled(initialDelay = 60_000 * 2 + 5_0000, fixedRate = 60_000 * 5 + 2_000)
    public void updateCommonStatuses() {
        final boolean websiteOnline = this.validateWebsite(this.websiteStatus.getUrl());
        this.websiteStatus.setCondition(websiteOnline ? Condition.GOOD : Condition.BAD);

        final boolean storeOnline = this.validateWebsite(this.storeStatus.getUrl());
        this.storeStatus.setCondition(storeOnline ? Condition.GOOD : Condition.BAD);

        this.overallStatus.updateStatus();
    }

    private boolean validateWebsite(final String url) {
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept-Language", "en-US,en;q=0.8")
                .addHeader("Referer", "https://www.google.com/")
                .build();

        try (final Response response = HttpClientUtilities.getOkHttpClient().newCall(request).execute()) {
            return response.isSuccessful();
        } catch (final IOException e) {
            CommonStatusService.log.warn("Exception during " + url, e);
            return false;
        }
    }

}
