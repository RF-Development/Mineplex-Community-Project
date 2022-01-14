package club.mineplex.api.tiktok;

import club.mineplex.api.utilities.HttpClientUtilities;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@Log4j2
@Tag(name = "TikTok")
public class TiktokController {

    @GetMapping("redirect")
    @Operation(summary = "Be weird")
    public Optional<String> getRedirectURL(@RequestParam("url") final String redirectURL) {
        final Request request = new Request.Builder()
                .url(redirectURL)
                .addHeader("Accept-Language", "en-US,en;q=0.8")
                .addHeader("Referer", "https://www.google.com/")
                .build();
        try (final Response response = HttpClientUtilities.getOkHttpClient().newCall(request).execute()) {
            if (response.isSuccessful()) {
                return Optional.of(response.request().url().toString());
            }

            return Optional.empty();
        } catch (final IOException e) {
            TiktokController.log.warn("Exception during " + redirectURL, e);
            return Optional.empty();
        }
    }
}
