package club.mineplex.api.mineplex.website.utilities;

import lombok.experimental.UtilityClass;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class WebsiteUtilities {
    private static final String BASE_URL = "https://www.mineplex.com";
    private static final Pattern MEMBER_PATTERN = Pattern.compile("members\\/.*\\.(\\d*)\\/");

    private static final Map<String, Integer> CUSTOM_URLS = new LinkedCaseInsensitiveMap<>();

    static {
        CUSTOM_URLS.put("toki", 83);
        CUSTOM_URLS.put("t3hero", 16);
        CUSTOM_URLS.put("bluebeetlehd", 18);
        CUSTOM_URLS.put("jarvis", 11);
        CUSTOM_URLS.put("emmalie", 108);
    }

    public String getBaseUrl() {
        return BASE_URL;
    }

    public Optional<Integer> getProfileIdFromCustomUrl(final String memberName) {
        return Optional.ofNullable(CUSTOM_URLS.get(memberName));
    }

    public Optional<Integer> getProfileId(final String memberUrl) {
        final Matcher memberMatcher = MEMBER_PATTERN.matcher(memberUrl);
        if (memberMatcher.find()) {
            return Optional.of(
                    Integer.parseInt(memberMatcher.group(1))
            );
        }

        return Optional.empty();
    }
}
