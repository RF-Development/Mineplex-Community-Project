package club.mineplex.api.mineplex.website.status.models.minecraft;

import club.mineplex.api.mineplex.website.status.models.ServerStatus;
import club.mineplex.api.utilities.FileUtilities;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class MinecraftServerStatus extends ServerStatus {

    private int onlinePlayers = -1;
    private int averagePing = -1;
    private String motd;

    @Setter(AccessLevel.NONE)
    private List<MinecraftRequestInfo> ranges;

    public MinecraftServerStatus(@NonNull final String server_name) {
        super(server_name);
        this.ranges = new ArrayList<>(this.loadRangesFromFile());
    }

    @SneakyThrows
    private Collection<MinecraftRequestInfo> loadRangesFromFile() {
        final File configFile =
                Paths.get("./configs/mineplex/status/" + this.serverName.toLowerCase() + "_ranges.json").toFile();
        final JsonMapper objectMapper = new JsonMapper();
        if (configFile.exists()) {
            return objectMapper.readValue(
                    configFile,
                    new TypeReference<Collection<MinecraftRequestInfo>>() {
                    }
            );
        } else {
            final List<MinecraftRequestInfo> defaultRanges = this.getDefaultRanges();
            FileUtilities.saveToFile(objectMapper, configFile, defaultRanges);
            return defaultRanges;
        }
    }

    private List<MinecraftRequestInfo> getDefaultRanges() {
        return Arrays.asList(
                MinecraftRequestInfo.ofDomain("us.mineplex.com"),
                MinecraftRequestInfo.ofDomain("cs.mineplex.com"),
                MinecraftRequestInfo.ofDomain("eu.mineplex.com"),
                MinecraftRequestInfo.ofDomain("mco.mineplex.com"),
                MinecraftRequestInfo.ofDomain("pe.mineplex.com")
        );
    }

}
