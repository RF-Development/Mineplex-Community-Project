package club.mineplex.discord;

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

public class Config {

    private final String file;
    private HashMap<String, Object> CONFIG = null;

    public Config(final String file) {
        this.file = file;

        this.createIfNotExists();
    }

    public static List<String> getAllFiles(final String path) {
        try (final Stream<Path> walk = Files.walk(Paths.get(path))) {

            final List<String> result = walk.map(x -> x.toString())
                                            .filter(f -> f.endsWith(".yml")).collect(Collectors.toList());

            return result;
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private void createIfNotExists() {
        final File toCheck = new File(this.file);
        if (!toCheck.exists()) {
            try {
                toCheck.createNewFile();
            } catch (final IOException e) {
                Main.log("Could not create \"" + this.file + "\n file!");
            }
        }
    }

    public void set(final String key, final Object value) {
        final HashMap<String, Object> data = (HashMap<String, Object>) this.CONFIG.clone();
        final String[] keys = key.split("\\.");

        if (keys.length == 1) {
            data.put(key, value);
        } else if (keys.length > 1) {

            HashMap<String, Object> index = (HashMap<String, Object>) data.get(keys[0]);
            for (int i = 1; i < keys.length; i++) {
                if (i + 1 == keys.length) {
                    index.put(keys[i], value);
                } else {
                    index = (HashMap<String, Object>) index.get(keys[i]);
                }
            }

        }

        final Yaml yaml = new Yaml();
        FileWriter writer = null;
        try {
            writer = new FileWriter(System.getProperty("user.dir") + "/" + this.file);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        yaml.dump(data, writer);
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
            Main.log("There was an error obtaining field \"" + path + "\" from \"" + this.file + "\"");
            Main.log("Please fix this and reload the file using \"reload\"");
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
