package club.mineplex.api.mineplex.website.profile;

import club.mineplex.api.mineplex.website.profile.models.*;
import club.mineplex.api.mineplex.website.utilities.AvatarUtilities;
import club.mineplex.api.mineplex.website.utilities.WebsiteUtilities;
import club.mineplex.api.utilities.HttpClientUtilities;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
public class ProfileService {
    private static final Pattern JAVA_UUID_PATTERN = Pattern.compile(
            "^\\/assets\\/www-mp\\/cached\\/skins\\/([\\da-f]{8}-(?:[\\da-f]{4}-){3}[\\da-f]{12})-render\\.jpg$");

    private Rank getRank(final String rankName) {
        if (rankName.isEmpty()) {
            return Rank.MEMBER;
        }

        return Rank.getRankByName(rankName).orElse(Rank.UNKOWN);
    }

    private ForumStatistic parseForumStatistic(final Document document) {
        int followers = 0;
        int following = 0;
        int posts = 0;
        int likes = 0;

        // Only the first two elements have a nice indicator by class name and checking the names itself is too costly
        final Elements statElements = document.select("div.MP_Cover_UserStats_stat_number");
        if (statElements.size() != 4) {
            log.warn("Found invalid stat elements of {}", statElements.size());
        }

        for (int index = 0; statElements.size() > index; index++) {
            final Element statElement = statElements.get(index);
            final String scoreText = statElement.text().replace(",", "");
            final int score = Integer.parseInt(scoreText);

            switch (index) {
                case 0:
                    followers = score;
                    break;
                case 1:
                    following = score;
                    break;
                case 2:
                    posts = score;
                    break;
                case 3:
                    likes = score;
                    break;
            }
        }

        return new ForumStatistic(
                followers,
                following,
                posts,
                likes
        );
    }

    private Optional<BedrockPlayer> parseBedrockPlayer(final Document document) {
        // Name
        final Element nameElement = document.selectFirst("div.MP_Prof_BedrockCharName_IGN");
        // Check if the bedrock account is linked
        if (nameElement == null) {
            return Optional.empty();
        }

        // Rank
        final Element rankElement = document.selectFirst("div.MP_Prof_BedrockCharName_Rank");
        final Rank rank = this.getRank(rankElement.text());

        return Optional.of(
                new BedrockPlayer(
                        rank,
                        nameElement.text()
                )
        );
    }

    private Optional<JavaPlayer> parseJavaPlayer(final Document document) {
        // Name
        final Element nameElement = document.selectFirst("div.MP_Prof_JavaCharName_IGN");
        // Check if the java account is linked
        if (nameElement == null) {
            return Optional.empty();
        }

        // Rank
        final Element rankElement = document.selectFirst("div.MP_Prof_JavaCharName_Rank");
        final Rank rank = this.getRank(rankElement.text());

        // UUID
        final Element avatarElement = document.selectFirst("img.MP_Prof_JavaCharSkin");
        final String avatarUrl = avatarElement.attr("src");
        final Matcher uuidMatcher = JAVA_UUID_PATTERN.matcher(avatarUrl);
        final UUID playerUUID;
        if (uuidMatcher.find()) {
            playerUUID = UUID.fromString(uuidMatcher.group(1));
        } else {
            log.warn("Can't find player uuid in {}", avatarUrl);
            return Optional.empty();
        }

        return Optional.of(
                new JavaPlayer(
                        rank,
                        nameElement.text(),
                        playerUUID
                )
        );
    }

    public Optional<EntireProfile> getEntireProfile(final int memberId) {
        final Optional<Document> documentOpt;
        try {
            documentOpt = HttpClientUtilities.getJsoupResponse(WebsiteUtilities.getBaseUrl() + "/members/" + memberId);
        } catch (final Exception e) {
            log.warn("Error while fetching profile", e);
            return Optional.empty();
        }
        if (!documentOpt.isPresent()) {
            return Optional.empty();
        }
        final Document document = documentOpt.get();

        // Forums
        final ForumStatistic forumStatistic = this.parseForumStatistic(document);
        final BedrockPlayer bedrockPlayer = this.parseBedrockPlayer(document).orElse(null);
        final JavaPlayer javaPlayer = this.parseJavaPlayer(document).orElse(null);

        // Get avatar
        final Element avatarElement = document.selectFirst("img.author");
        final String avatarUrl = avatarElement.attr("src");
        final ProfileAvatar avatar = AvatarUtilities.getProfileAvatar(avatarUrl, memberId);

        // Name
        final Element nameElement = document.selectFirst("div.MP_Profile_Username");
        final String playerName = nameElement.text();

        return Optional.of(
                new EntireProfile(
                        memberId,
                        avatar,
                        playerName,
                        bedrockPlayer,
                        javaPlayer,
                        forumStatistic
                )
        );
    }
}
