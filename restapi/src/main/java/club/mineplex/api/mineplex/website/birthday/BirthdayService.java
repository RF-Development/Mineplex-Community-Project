package club.mineplex.api.mineplex.website.birthday;

import club.mineplex.api.mineplex.website.birthday.models.Birthday;
import club.mineplex.api.mineplex.website.profile.models.Profile;
import club.mineplex.api.mineplex.website.profile.models.ProfileAvatar;
import club.mineplex.api.mineplex.website.utilities.AvatarUtilities;
import club.mineplex.api.mineplex.website.utilities.WebsiteUtilities;
import club.mineplex.api.utilities.HttpClientUtilities;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Log4j2
public class BirthdayService {
    @Getter
    private final Set<Birthday> birthdays = Collections.synchronizedSet(new HashSet<>());

    private Optional<Profile> parseMember(final Element member) {
        final Element info = member.selectFirst("a");
        final String playerName = info.attr("title");
        if (playerName == null) {
            log.warn("Can't find player name in {}", info);
            return Optional.empty();
        }

        // Profile id
        final Element linkElement = info.selectFirst("a");
        final String link = linkElement.attr("href");

        // TODO: Make sure that this page is not using the custom urls
        final Optional<Integer> profileIdOpt = WebsiteUtilities.getProfileId(link);
        if (!profileIdOpt.isPresent()) {
            log.warn("Can't find profile id in {}", member);
            return Optional.empty();
        }
        final int profileId = profileIdOpt.get();

        // Profile Avatar
        final Element avatar = member.selectFirst("span");
        final String avatarUrl = avatar.attr("style");
        final ProfileAvatar profileAvatar = AvatarUtilities.getProfileAvatar(avatarUrl, profileId);

        return Optional.of(
                new Profile(
                        profileId,
                        profileAvatar,
                        playerName
                )
        );
    }

    @Scheduled(fixedRateString = "PT1H")
    private void updateBirthdays() {
        log.info("Updating birthdays");

        final Optional<Document> documentOpt;
        try {
            documentOpt = HttpClientUtilities.getJsoupResponse(WebsiteUtilities.getBaseUrl() + "/members/");
        } catch (final IOException e) {
            log.warn("Error while fetching members page", e);
            return;
        }
        if (!documentOpt.isPresent()) {
            return;
        }

        final Document document = documentOpt.get();
        final Element birthday = document.selectFirst("div.secondaryContent.avatarHeap");

        if (birthday == null) {
            log.warn("Can't find birthday container");
            return;
        }

        final String birthdayText = birthday.text();
        if (!birthdayText.startsWith("Today's Birthdays")) {
            log.warn("Can't find any birthdays");
            return;
        }

        this.birthdays.clear();

        for (final Element member : birthday.select("li")) {
            this.parseMember(member)
                .map(Birthday::new)
                .ifPresent(this.birthdays::add);
        }
        log.info("Found {} birthdays", this.birthdays.size());
    }
}
