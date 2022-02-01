package club.mineplex.discord.commands.all.content;

import lombok.SneakyThrows;

import java.net.URL;
import java.util.regex.Pattern;

public enum ContentType {

    YOUTUBE("https://upload.wikimedia.org/wikipedia/commons/thumb/0/09/YouTube_full-color_icon_%282017%29.svg/2560px-YouTube_full-color_icon_%282017%29.svg.png",
            "(?:http(?:s)?:\\/\\/)?(?:www\\.)?(?:youtu\\.be\\/|youtube\\.com\\/(?:(?:watch)?\\?(?:.*&)?v(?:i)?=|(?:embed|v|vi|user)\\/))([^\\?&\\\"'<> #]+)"
    ),
    TWITCH("https://cdn-3.expansion.mx/dims4/default/335373d/2147483647/strip/true/crop/1280x640+0+0/resize/1800x900!/format/webp/quality/90/?url=https%3A%2F%2Fcdn-3.expansion.mx%2Ff6%2Fcc%2F84ccf33047b189eb29e32655d037%2Ftwitch-politica-de-odio.jpg",
           "(?:http|https)://(?:\\w+.)?twitch\\.(?:tv|com)(?:/([^\\s]+))?"
    );

    private final Pattern pattern;
    private final URL imageUrl;

    @SneakyThrows
    ContentType(final String imageUrl, final String regex) {
        this.imageUrl = new URL(imageUrl);
        this.pattern = Pattern.compile(regex);
    }

    public Pattern getPattern() {
        return this.pattern;
    }

    public URL getImageUrl() {
        return this.imageUrl;
    }

}
