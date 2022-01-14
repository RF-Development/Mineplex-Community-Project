package club.mineplex.api.mineplex.mods.clans;

import club.mineplex.api.mineplex.mods.clans.deserializers.ReleaseInfoInfoDeserializer;
import club.mineplex.api.mineplex.mods.clans.models.ReleaseInfo;
import club.mineplex.api.utilities.HttpClientUtilities;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class ClansModService {
    private static final String REPOSITORY = "Mineplex-Community/Clans-Mod/releases";

    private final ObjectMapper objectMapper;
    private ReleaseInfo releaseInfo;

    public ClansModService() {
        this.objectMapper = JsonMapper.builder()
                                      .addModule(new Jdk8Module())
                                      .addModule(
                                              new SimpleModule()
                                                      .addDeserializer(ReleaseInfo.class,
                                                                       new ReleaseInfoInfoDeserializer()
                                                      )
                                      )
                                      .build();
    }

    @Scheduled(fixedRateString = "PT1H")
    private void updateReleaseInfo() {
        try {
            final Optional<ReleaseInfo> releaseInfoOpt = HttpClientUtilities.getParsedResponse(
                    "https://api.github.com/repos/" + REPOSITORY,
                    this.objectMapper,
                    ReleaseInfo.class
            );
            if (releaseInfoOpt.isPresent()) {
                this.releaseInfo = releaseInfoOpt.get();
            } else {
                log.info("Can't find release info");
            }

        } catch (final Exception e) {
            log.warn("Exception while updating clans mod release info", e);
        }
    }

    public Optional<ReleaseInfo> getReleaseInfo() {
        return Optional.ofNullable(this.releaseInfo);
    }

    public String getReleaseUrl() {
        return "https://github.com/" + REPOSITORY;
    }
}
