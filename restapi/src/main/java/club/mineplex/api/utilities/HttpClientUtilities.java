package club.mineplex.api.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class HttpClientUtilities {
    private final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36";

    private OkHttpClient client = null;

    private Request constructRequest(final String url) {
        return new Request.Builder()
                .url(url)
                .build();
    }

    public OkHttpClient getOkHttpClient() {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(chain -> {
                        final Request originalRequest = chain.request();
                        final Request requestWithUserAgent = originalRequest.newBuilder()
                                .header("User-Agent", USER_AGENT)
                                .build();
                        return chain.proceed(requestWithUserAgent);
                    })
                    .build();
        }
        return client;
    }

    public Optional<Document> getJsoupResponse(final String url) throws IOException {
        final Request request = constructRequest(url);
        try (final Response response = HttpClientUtilities.getOkHttpClient().newCall(request).execute()) {
            if (response.isSuccessful()) {
                try (final InputStream inputStream = response.body().byteStream()) {
                    return Optional.of(Jsoup.parse(inputStream, null, url));
                }
            }

            return Optional.empty();
        }
    }

    public <T> Optional<T> getParsedResponse(final String url, final ObjectMapper objectMapper, final Class<T> clazz) throws IOException {
        final Request request = constructRequest(url);
        try (final Response response = HttpClientUtilities.getOkHttpClient().newCall(request).execute()) {
            if (response.isSuccessful()) {
                try (final InputStream inputStream = response.body().byteStream()) {
                    final T parsed = objectMapper.readValue(inputStream, clazz);
                    return Optional.ofNullable(parsed);
                }
            }

            return Optional.empty();
        }
    }
}
