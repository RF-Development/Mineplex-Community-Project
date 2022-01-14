package club.mineplex.api.mineplex.mods.clans;

import club.mineplex.api.mineplex.website.profile.ProfileService;
import club.mineplex.api.mineplex.website.profile.models.EntireProfile;
import club.mineplex.api.mineplex.website.profile.models.JavaPlayer;
import club.mineplex.api.mineplex.website.profile.models.Profile;
import club.mineplex.api.mineplex.website.staffteam.StaffService;
import club.mineplex.api.mineplex.website.subteam.SubteamService;
import club.mineplex.api.mineplex.website.subteam.models.Subteam;
import club.mineplex.api.utilities.FileUtilities;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/*
 In order to add a new column on the spreadsheet,
 you have to add the column name to to columnsToExtract.

 In order to add a new mineplex rank or team to include in this
 handler, add it to mineplexTeams array.
 */
@Service
@Log4j2
public class ModRolesService {
    private static final String[] MINEPLEX_TEAMS = {
            "Clans Management",
            "Clans Management Assistant"
    };

    @Getter
    private final Map<String, List<UUID>> modRoles = new ConcurrentHashMap<>();

    private final StaffService staffService;
    private final ProfileService profileService;
    private final SubteamService subteamService;

    public ModRolesService(final StaffService staffService, final ProfileService profileService,
                           final SubteamService subteamService) {
        this.staffService = staffService;
        this.profileService = profileService;
        this.subteamService = subteamService;

        // Load roles from file
        this.modRoles.putAll(this.loadRolesFromFile());
    }

    @SneakyThrows
    private Map<String, List<UUID>> loadRolesFromFile() {
        final File configFile = Paths.get("./configs/mods/clans/roles.json").toFile();
        final ObjectMapper objectMapper = new ObjectMapper();
        if (configFile.exists()) {
            return objectMapper.readValue(
                    configFile,
                    new TypeReference<Map<String, List<UUID>>>() {
                    }
            );
        } else {
            final Map<String, List<UUID>> defaultFeatures = this.getDefaultRoles();
            FileUtilities.saveToFile(objectMapper, configFile, defaultFeatures);
            return defaultFeatures;
        }
    }

    private Map<String, List<UUID>> getDefaultRoles() {
        final Map<String, List<UUID>> roles = new HashMap<>();

        roles.put("Ideas", new ArrayList<>());
        roles.put("Developer", new ArrayList<>());
        roles.put("Clans Insights", new ArrayList<>());

        return roles;
    }

    private List<Profile> getRankProfiles(final String rankName) {
        final List<Profile> profiles = this.staffService.getStaffMembers().get(rankName);
        if (profiles != null) {
            return profiles;
        }

        final Optional<Subteam> subteamOpt = this.subteamService.getByName(rankName);
        if (subteamOpt.isPresent()) {
            return Arrays.asList(subteamOpt.get().getMembers());
        }

        return Collections.emptyList();
    }

    @Scheduled(fixedRateString = "P1D")
    private void updateModRoles() {
        //  We replace the specific ranks every interval with the new players
        for (final String rank : MINEPLEX_TEAMS) {
            final List<Profile> members = this.getRankProfiles(rank);
            final List<UUID> players = Lists.newArrayListWithCapacity(members.size());
            for (final Profile member : members) {
                this.profileService.getEntireProfile(member.getMemberId())
                                   .map(EntireProfile::getJavaPlayer)
                                   .filter(Optional::isPresent)
                                   .map(Optional::get)
                                   .map((JavaPlayer::getUuid))
                                   .ifPresent(players::add);
            }
            this.modRoles.put(rank, players);
        }
    }
}
