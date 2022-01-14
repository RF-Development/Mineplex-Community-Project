package club.mineplex.api.mineplex.mods.clans;

import club.mineplex.api.mineplex.mods.clans.models.Feature;
import club.mineplex.api.utilities.FileUtilities;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

@Service
@Log4j2
public class FeatureService {
    private final Map<String, Feature> features;

    public FeatureService() {
        final Collection<Feature> loadedFeatures = this.loadFeaturesFromFile();
        this.features = new LinkedCaseInsensitiveMap<>(loadedFeatures.size());
        for (final Feature feature : loadedFeatures) {
            this.features.put(
                    feature.getName(),
                    feature
            );
        }
    }

    @SneakyThrows
    private Collection<Feature> loadFeaturesFromFile() {
        final File configFile = Paths.get("./configs/mods/clans/features.json").toFile();
        final ObjectMapper objectMapper = new ObjectMapper();
        if (configFile.exists()) {
            return objectMapper.readValue(
                    configFile,
                    new TypeReference<Collection<Feature>>() {
                    }
            );
        } else {
            final Collection<Feature> defaultFeatures = this.getDefaultFeatures();
            FileUtilities.saveToFile(objectMapper, configFile, defaultFeatures);
            return defaultFeatures;
        }
    }

    private Collection<Feature> getDefaultFeatures() {
        return Arrays.asList(
                Feature.ofPublic("drop-prevention"),
                Feature.ofPublic("enhanced-mounts"),
                Feature.ofPublic("mineplex-server-checker"),
                Feature.ofPublic("slot-lock"),
                Feature.ofPublic("message-filter")
        );
    }

    public List<Feature> getFeatures() {
        return new ArrayList<>(this.features.values());
    }
}
