package club.mineplex.api.mineplex.website.subteam;

import club.mineplex.api.mineplex.website.profile.models.Profile;
import club.mineplex.api.mineplex.website.profile.models.ProfileAvatar;
import club.mineplex.api.mineplex.website.subteam.models.Subteam;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Getter
@Service
@Log4j2
public class SubteamService {
    private final List<Subteam> subteams = Collections.synchronizedList(new ArrayList<>());

    private Optional<Integer> getProfileId(final Element memberElement, final String playerName) {
        final Optional<Integer> memberIdOpt = WebsiteUtilities.getProfileId(memberElement.attr("href"));
        if (memberIdOpt.isPresent()) {
            return memberIdOpt;
        }

        final Optional<Integer> customMemberIdOpt = WebsiteUtilities.getProfileIdFromCustomUrl(playerName);
        if (customMemberIdOpt.isPresent()) {
            return customMemberIdOpt;
        }

        return Optional.empty();
    }

    @Scheduled(fixedRateString = "PT1H")
    private void updateSubteams() {
        log.info("Updating subteam list");

        final Optional<Document> documentOpt;
        try {
            documentOpt = HttpClientUtilities.getJsoupResponse(WebsiteUtilities.getBaseUrl() + "/subteams/?show=all");
        } catch (final Exception e) {
            log.warn("Error while fetching subteam page", e);
            return;
        }
        if (!documentOpt.isPresent()) {
            return;
        }

        this.subteams.clear();
        final Document document = documentOpt.get();
        for (final Element category : document.select("li.MedalCategory")) {
            final Element categoryElement = category.selectFirst("h3.categoryName");
            if (categoryElement == null) {
                log.warn("Can't find category name inside {}", category);
                continue;
            }

            final String categoryName = categoryElement.text();
            for (final Element teamElement : category.select("div.info")) {
                // Parse members
                final Elements memberElements = teamElement.select("a.username");
                final List<Profile> members = Lists.newArrayListWithCapacity(memberElements.size());
                for (final Element memberElement : memberElements) {
                    final String playerName = memberElement.text();
                    final Optional<Integer> memberIdOpt = this.getProfileId(memberElement, playerName);
                    if (!memberIdOpt.isPresent()) {
                        log.warn("Can't find member inside {}", memberElement);
                        continue;
                    }

                    final Profile profile = new Profile(
                            memberIdOpt.get(),
                            ProfileAvatar.ofDefault(),
                            playerName
                    );
                    members.add(profile);
                }

                final String teamName = teamElement.selectFirst("h3.name").text();
                final String description = teamElement.selectFirst("p.description").text();

                final Subteam subteam = new Subteam(
                        members.toArray(new Profile[0]),
                        categoryName,
                        teamName,
                        description
                );
                log.info("Found {} members for {}", members.size(), teamName);
                this.subteams.add(subteam);
            }
        }
    }

    public Optional<Subteam> getByName(final String name) {
        for (final Subteam subteam : this.subteams) {
            if (subteam.getName().equalsIgnoreCase(name)) {
                return Optional.of(subteam);
            }
        }
        return Optional.empty();
    }
}
