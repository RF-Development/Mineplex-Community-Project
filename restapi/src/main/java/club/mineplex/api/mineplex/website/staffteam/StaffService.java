package club.mineplex.api.mineplex.website.staffteam;

import club.mineplex.api.mineplex.website.profile.models.Profile;
import club.mineplex.api.mineplex.website.profile.models.ProfileAvatar;
import club.mineplex.api.mineplex.website.utilities.AvatarUtilities;
import club.mineplex.api.mineplex.website.utilities.WebsiteUtilities;
import club.mineplex.api.utilities.HttpClientUtilities;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
public class StaffService {
    private static final Pattern MEMBER_PATTERN =
            Pattern.compile("^https:\\/\\/www\\.mineplex\\.com\\/members\\/(\\d*)\\/$");

    @Getter
    private final Map<String, List<Profile>> staffMembers =
            Collections.synchronizedMap(new LinkedCaseInsensitiveMap<>());

    private ProfileAvatar getProfileAvatar(final Element member, final int profileId) {
        final Element avatar = member.selectFirst("img");
        final String avatarUrl = avatar.attr("src");
        return AvatarUtilities.getProfileAvatar(avatarUrl, profileId);
    }

    private Optional<Profile> parseMember(final Element member) {
        final String memberUrl = member.attr("href");
        final Matcher memberMatcher = MEMBER_PATTERN.matcher(memberUrl);
        if (!memberMatcher.find()) {
            log.warn("Can't find member id inside {}", memberUrl);
            return Optional.empty();
        }

        final int memberId = Integer.parseInt(memberMatcher.group(1));
        final ProfileAvatar avatar = this.getProfileAvatar(member, memberId);
        final String playerName = member.text();

        final Profile user = new Profile(memberId, avatar, playerName);
        return Optional.of(user);
    }

    @Scheduled(fixedRateString = "PT1H")
    private void updateStaffs() {
        log.info("Updating staff list");

        final Optional<Document> documentOpt;
        try {
            documentOpt = HttpClientUtilities.getJsoupResponse(
                    WebsiteUtilities.getBaseUrl() + "/assets/www-mp/sym/staffCaching.php");
        } catch (final Exception e) {
            log.warn("Error while fetching staff page", e);
            return;
        }
        if (!documentOpt.isPresent()) {
            return;
        }

        final Document document = documentOpt.get();

        this.staffMembers.clear();
        for (final Element rankSection : document.select("div.www-mp-staff-type")) {
            final Element rankElement = rankSection.selectFirst("h3");
            if (rankElement == null) {
                log.warn("Can't find rank inside {}", rankSection);
                continue;
            }

            // Parse members
            final Elements membersElements = rankSection.select("a");
            final List<Profile> members = Lists.newArrayListWithCapacity(membersElements.size());
            for (final Element member : membersElements) {
                this.parseMember(member)
                    .ifPresent(members::add);
            }

            final String rankName = rankElement.text();
            this.staffMembers.put(rankName, members);
            log.info(
                    "Found {} members for {}",
                    members.size(),
                    rankName
            );
        }
    }
}
