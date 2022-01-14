package club.mineplex.discord;

import club.mineplex.discord.objects.ConnectionBuilder;
import net.dv8tion.jda.api.entities.Emote;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BotUtil {

    public static String agent = "Chrome";
    public static String accept = "*/*";

    public static InputStream imageFromUrl(final String url) {
        if (url == null) {
            return null;
        }
        try {
            final URL u = new URL(url);
            final URLConnection urlConnection = u.openConnection();
            urlConnection.setRequestProperty("user-agent",
                                             "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36"
            );
            return urlConnection.getInputStream();
        } catch (final IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean isInteger(final String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    public static Emote getEmoji(final long id) {
        return Main.getJDA().getEmoteById(id);
    }

    public static String getEmojiAsMention(final long id) {
        return Main.getJDA().getEmoteById(id) == null ? ":x:" : Main.getJDA().getEmoteById(id).getAsMention();
    }

    public static String formatDate(final OffsetDateTime time) {
        return time.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    }

    public static Set<Long> getCommandChannels() {
        return new HashSet<>((List<Long>) Main.config.getOrDefault("cmd-channels", ArrayList.class, new ArrayList<>()));
    }

    public static String scrape(final String urlString) throws Exception {
        final URL url = new URL(urlString);
        final URLConnection connection = url.openConnection();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        String line = null, data = "";

        while ((line = reader.readLine()) != null) {
            data += line + "\n";
        }

        return data;
    }

    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     */
    public static double similarity(final String s1, final String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) {
            longer = s2;
            shorter = s1;
        }
        final int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0;
        }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        final int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                            newValue = Math.min(Math.min(newValue, lastValue),
                                                costs[j]
                            ) + 1;
                        }
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
    }

    public static String mineplexScrape(final String url) throws IOException {

        final ConnectionBuilder conn = new ConnectionBuilder(url).method("GET");

        conn.header("Accept", BotUtil.accept);
        conn.header("Origin", "https://mineplex.com/");
        conn.header("User-Agent", BotUtil.agent);

        conn.send();

        /*
        System.setProperty("https.proxyHost", "45.142.28.96");
        System.setProperty("https.proxyPort", "80");

        System.setProperty("https.proxyUser", "oalbjinj-dest");
        System.setProperty("https.proxyPassword", "o79rppvw2irq");

        String ret = Jsoup.connect(url)
                .method(Connection.Method.GET)
                .timeout(10 * 1000)
                .header("Accept", BotUtil.accept)
                .header("Origin", "https://mineplex.com/")
                .referrer("https://google.com/")
                .ignoreHttpErrors(true)
                .followRedirects(true)
                .userAgent(BotUtil.agent)
                .get()
                .html();
        return ret;
         */
        return conn.getResponseString();
    }

    public static int getURLCode(final HttpURLConnection connection) {
        try {
            return connection.getResponseCode();
        } catch (final IOException | NullPointerException e) {
            return 400;
        }
    }

    public static String getURLResponse(final HttpURLConnection connection) {
        if (connection == null) {
            return "Error grabbing response!";
        }

        try {
            if (connection.getResponseCode() > 199 && connection.getResponseCode() < 400) {

                final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String finalRes = "";
                String line;

                while ((line = reader.readLine()) != null) {
                    finalRes += line;
                }

                reader.close();

                return finalRes;

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

    public static class BotProxy {

        public String ip, username = null, password = null;
        public int port;

        public Proxy proxy;

        public BotProxy(final String ip, final int port) {
            this.ip = ip;
            this.port = port;

            this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
        }

        public void setPassword(final String password) {
            this.password = password;
        }

        public void setUsername(final String username) {
            this.username = username;
        }

        public boolean check() {
            try {
                System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "false");
                System.setProperty("jdk.http.auth.proxying.disabledSchemes", "false");

                final URL u = new URL("https://google.com/");
                final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.ip, this.port));
                final HttpURLConnection uc = (HttpURLConnection) u.openConnection(proxy);

                if (this.useAuth()) {
                    Authenticator.setDefault(new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            if (this.getRequestorType().equals(RequestorType.PROXY)) {
                                return new PasswordAuthentication(BotProxy.this.username,
                                                                  BotProxy.this.password.toCharArray()
                                );
                            }
                            return super.getPasswordAuthentication();
                        }
                    });
                }

                uc.connect();
                return true;
            } catch (final Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean useAuth() {
            return this.password != null && this.username != null;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof BotProxy) {
                final BotProxy sP = (BotProxy) obj;
                return sP.ip.equalsIgnoreCase(this.ip);
            }
            return false;
        }

        @Override
        public String toString() {
            if (this.proxy != null) {
                return ((InetSocketAddress) this.proxy.address()).getHostName() + ":" + this.port;
            }
            return "null";
        }

    }

    public static class BufferedImageUtils {
        public static BufferedImage getAndScaleProfilePicture(final URL avatarURL, final Integer size) throws
                IOException {

            BufferedImage avatarImage = ImageIO.read(BotUtil.imageFromUrl(avatarURL.toString()));
            avatarImage = scaleImage(avatarImage, size, size, Color.BLACK);

            if (avatarImage == null) {
                System.out.println("Failed at BufferedImageUtils");
            }
            return avatarImage;

        }

        public static BufferedImage scaleImage(final BufferedImage img, int width, int height, final Color background) {

            final int imgWidth = img.getWidth();
            final int imgHeight = img.getHeight();

            if (imgWidth * height < imgHeight * width) {
                width = imgWidth * height / imgHeight;
            } else {
                height = imgHeight * width / imgWidth;
            }

            final BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            final Graphics2D g = newImage.createGraphics();

            try {
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g.setBackground(background);
                g.clearRect(0, 0, width, height);
                g.drawImage(img, 0, 0, width, height, null);

            } finally {
                g.dispose();
            }

            return newImage;
        }

        public static String getFormattedPosition(final int date) {

            switch (date) {
                case 1:
                case 21:
                case 31:
                    return "" + date + "st";

                case 2:
                case 22:
                    return "" + date + "nd";

                case 3:
                case 23:
                    return "" + date + "rd";

                default:
                    return "" + date + "th";
            }

        }

        public static void addImage(final BufferedImage buff1, final BufferedImage buff2,
                                    final float opaque, final int x, final int y) {
            final Graphics2D g2d = buff1.createGraphics();
            g2d.setComposite(
                    AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opaque));
            g2d.drawImage(buff2, x, y, null);
            g2d.dispose();


        }


    }

}
