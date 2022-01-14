package club.mineplex.milestones.configs;

import club.mineplex.milestones.utilities.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigLoader {

    private HashMap<String, Object> CONFIG = null;
    private final String file;

    public ConfigLoader(final String file) {
        this.file = file;
        this.createIfNotExists();
    }

    public static List<String> getAllFiles(final String path) {
        try (final Stream<Path> walk = Files.walk(Paths.get(path))) {

            return walk.map(Path::toString)
                       .filter(f -> f.endsWith(".yml")).collect(Collectors.toList());
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private void createIfNotExists() {
        final File toCheck = new File(this.file);

        if (!toCheck.exists()) {
            try {
                if (!toCheck.createNewFile()) {
                    throw new IOException("Failed to create");
                }
            } catch (final IOException e) {
                Logger.log("Could not create \"" + this.file + "\n file!", Logger.LogLevel.ERROR);
            }
        }
    }

    public <T> T getOrDefault(final String path, final Class<T> type, final T defaultV) {
        return (this.get(path, type) == null) ? defaultV : this.get(path, type);
    }

    public <T> T get(final String path, final Class<T> type) {

        try {
            final String[] keys = path.split("\\.");

            HashMap<String, Object> index = (HashMap<String, Object>) this.CONFIG.clone();
            for (int i = 0; i < keys.length; i++) {
                if (i + 1 == keys.length) {
                    return type.cast(index.get(keys[i]));
                } else {
                    index = (HashMap<String, Object>) index.get(keys[i]);
                }
            }

            return null;
        } catch (final Exception e) {
            Logger.log("There was an error obtaining field \"" + path + "\" from \"" + this.file + "\"",
                       Logger.LogLevel.ERROR
            );
            Logger.log("Please fix this and reload the file using \"reload\"", Logger.LogLevel.ERROR);
            e.printStackTrace();
            return null;
        }
    }

    public boolean load() {
        final Yaml yaml = new Yaml();
        final InputStream inputStream;
        try {
            inputStream = new FileInputStream(System.getProperty("user.dir") + "/" + this.file);
        } catch (final FileNotFoundException e) {
            return false;
        }
        this.CONFIG = yaml.load(inputStream);
        return true;
    }

    public boolean reload() {
        return this.load();
    }
}
