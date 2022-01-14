package club.mineplex.core.discord;

import club.mineplex.core.util.HttpClientUtilities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

@Value
@Builder
@Log4j2
public class WebhookMessage {

    @NonNull
    @JsonIgnore
    String url;

    String avatar_url;
    String content;
    String username;
    Embed[] embeds;

    @SneakyThrows
    public void post() {
        final ObjectMapper mapper = new ObjectMapper();
        final String payload = mapper.writeValueAsString(this);

        final RequestBody body = RequestBody.create(MediaType.parse("application/json"), payload);
        final Request request = new Request.Builder().url(this.url).post(body).build();
        try {
            final Response response = HttpClientUtilities.getOkHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                return;
            }
            response.close();
        } catch (final IOException e) {
            log.error("There was an error sending a webhook with '{}'", this.url);
            e.printStackTrace();
        }
    }

}
