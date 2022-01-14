package club.mineplex.discord.objects;

import club.mineplex.discord.BotUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.SocketException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class ConnectionBuilder {

    private final static String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
    private final Map<String, String> headers;
    public long delay = 0, sentTime = 0;
    private String endpoint;
    private String data;
    private String method;
    private Proxy proxy;
    private HttpURLConnection finalConnection;

    public ConnectionBuilder(final String endpoint) {

        this.data = "";
        this.method = "GET";
        this.endpoint = endpoint;
        this.headers = new HashMap<>();

    }

    public void proxy(final Proxy proxy) {

        this.proxy = proxy;

    }

    public void endpoint(final String endpoint) {

        this.endpoint = endpoint;

    }

    public void header(final String key, final String value) {

        this.headers.put(key, value);

    }

    public ConnectionBuilder method(final String method) {

        this.method = method;
        return this;

    }

    public void data(final String data) {

        this.data = data;

    }

    public void send() {

        this.sentTime = Instant.now().toEpochMilli();

        try {

            final HttpURLConnection connection = this.ssl();

            connection.setRequestProperty("User-Agent", USER_AGENT);

            for (final Map.Entry<String, String> entry : this.headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            if (this.method.equalsIgnoreCase("POST")) {

                connection.setConnectTimeout(10_000);
                connection.setReadTimeout(10_000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                connection.setRequestMethod("POST");
                connection.connect();

                final DataOutputStream output = new DataOutputStream(connection.getOutputStream());
                output.writeBytes(this.data);
                output.flush();
                output.close();

                this.finalConnection = connection;

            } else if (this.method.equalsIgnoreCase("GET")) {

                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10_000);
                connection.setDoOutput(true);

                connection.getResponseCode();
                connection.connect();

                this.finalConnection = connection;
            } else if (this.method.equalsIgnoreCase("PUT")) {

                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);

                connection.connect();

                final OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(this.data);
                osw.flush();
                osw.close();

                this.finalConnection = connection;
            }


        } catch (final SocketException e) {
            System.out.println();
            System.out.println("[SocketException] " + e.getMessage());
            System.exit(0);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        this.delay = Instant.now().toEpochMilli() - this.sentTime;
    }

    private HttpURLConnection ssl() throws IOException {
        return BotUtil.ssl(this.endpoint, this.proxy);
    }

    public String getResponseString() {
        return BotUtil.getURLResponse(this.finalConnection);
    }

    public String getHeader(final String name) {

        return this.finalConnection.getHeaderField(name);

    }

    public int getResponseCode() {
        return BotUtil.getURLCode(this.finalConnection);
    }

    public HttpURLConnection getFinalConnection() {
        return this.finalConnection;
    }

    public Map<String, String> getSentHeaders() {
        return this.headers;
    }

    private String formatGetURL(final String url, final String data) {

        String newURL = url;

        if (!newURL.endsWith("?")) {
            newURL = newURL + "?";
        }

        newURL = newURL + data;

        return newURL;

    }

}
