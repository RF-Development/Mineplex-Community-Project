package club.mineplex.milestones.utilities;

import club.mineplex.milestones.Main;
import club.mineplex.milestones.utilities.objects.discord.ConnectionBuilder;
import net.dv8tion.jda.api.entities.Emote;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;

public class BotUtil {

    /**
     * Convert a number to a word
     * Via https://stackoverflow.com/questions/3911966/how-to-convert-number-to-words-in-java
     */
    private static final String[] tensNames = {
            "",
            " ten",
            " twenty",
            " thirty",
            " forty",
            " fifty",
            " sixty",
            " seventy",
            " eighty",
            " ninety"
    };
    private static final String[] numNames = {
            "",
            " one",
            " two",
            " three",
            " four",
            " five",
            " six",
            " seven",
            " eight",
            " nine",
            " ten",
            " eleven",
            " twelve",
            " thirteen",
            " fourteen",
            " fifteen",
            " sixteen",
            " seventeen",
            " eighteen",
            " nineteen"
    };

    public static boolean userIsStaff(final JSONObject userData) {

        boolean isStaff = false;

        if (userData.has("javaData")) {
            final String userRank = userData.getJSONObject("javaData").getString("rank");

            // [trainee, mod, sr.mod] contains [mod] ?
            if (Main.config.staffRanks.contains(userRank.toLowerCase(Locale.ROOT))) {
                isStaff = true;
            }
        }

        if (userData.has("bedrockData")) {
            final String userRank = userData.getJSONObject("bedrockData").getString("rank");
            if (Main.config.staffRanks.contains(userRank.toLowerCase(Locale.ROOT))) {
                isStaff = true;
            }
        }

        return isStaff;

    }

    public static String scrapeWebsite(final String urlString) {
        /*
        StringBuilder data = null;

        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            final InputStreamReader r = new InputStreamReader(connection.getInputStream());
            final BufferedReader reader = new BufferedReader(r);
            String line;
            data = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                data.append(line).append("\n");
            }

            reader.close();
            r.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (data != null) {
            return data.toString();
        }
         */

        final ConnectionBuilder conn = new ConnectionBuilder(urlString);
        conn.send();
        return conn.getResponseString();
    }

    /**
     * Get a discord Emoji object given its ID
     *
     * @param id ID of the emoji
     * @return The emote
     */
    public static Emote getEmoji(final long id) {
        return Main.getJDA().getEmoteById(id);
    }

    /**
     * Get a discord Emoji as a mention given its ID
     *
     * @param id The ID of the emoji
     * @return The emoji mention
     */
    public static String getEmojiAsMention(final long id) {
        final Emote emote = getEmoji(id);
        return (emote == null) ? "<null-emote>" : emote.getAsMention();
    }

    /**
     * Get the URL code from an HTTP connection
     *
     * @param connection Connection that was made
     * @return URL code int
     */
    public static int getURLCode(final HttpURLConnection connection) {
        try {
            return connection.getResponseCode();
        } catch (final IOException | NullPointerException e) {
            return 400;
        }
    }

    /**
     * Get the response from a URL
     *
     * @param connection The HTTP connection
     * @return String reply (perhaps JSON?)
     */
    public static String getURLResponse(final HttpURLConnection connection) {
        if (connection == null) {
            return "Error grabbing response!";
        }

        try {
            if (connection.getResponseCode() > 199 && connection.getResponseCode() < 400) {

                final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                final StringBuilder finalRes = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    finalRes.append(line);
                }

                reader.close();

                return finalRes.toString();

            } else {

                final BufferedReader reader;
                try {
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                } catch (final NullPointerException x) {
                    return "Error grabbing response";
                }

                final StringBuilder finalRes = new StringBuilder();
                String line;

                try {
                    while ((line = reader.readLine()) != null) {
                        finalRes.append(line);
                    }
                } catch (final IOException e1) {
                    return "Error grabbing response";
                }

                try {
                    reader.close();
                } catch (final IOException e1) {
                    return "Error grabbing response";
                }

                return finalRes.toString();

            }
        } catch (final IOException e) {
            return "Error grabbing response";
        }
    }

    public static HttpURLConnection ssl(final String url, final Proxy proxy) throws IOException {

        final HttpURLConnection connection;
        CookieHandler.setDefault(new CookieManager());
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
            }
        }};
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            assert sc != null;
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (final KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        if (proxy != null) {
            connection = (HttpsURLConnection) new URL(url).openConnection(proxy);
        } else {
            connection = (HttpsURLConnection) new URL(url).openConnection();
        }

        connection.setInstanceFollowRedirects(false);
        return connection;
    }

    public static long getTimeUntil(final ZoneId timezone, final int hour, final int minutes, final int seconds) {
        final ZonedDateTime now = ZonedDateTime.now(timezone);
        ZonedDateTime nextRun = now.withHour(hour).withMinute(minutes).withSecond(seconds);
        if (now.compareTo(nextRun) > 0) {
            nextRun = nextRun.plusDays(1);
        }

        final Duration duration = Duration.between(now, nextRun);
        return duration.getSeconds();
    }

    public static String numberToWordsUnder1K(int number) {
        String soFar;

        if (number % 100 < 20) {
            soFar = numNames[number % 100];
            number /= 100;
        } else {
            soFar = numNames[number % 10];
            number /= 10;

            soFar = tensNames[number % 10] + soFar;
            number /= 10;
        }
        if (number == 0) {
            return soFar.replaceAll(" ", "");
        }
        return numNames[number].replaceAll(" ", "") + " hundred" + soFar;
    }

}
