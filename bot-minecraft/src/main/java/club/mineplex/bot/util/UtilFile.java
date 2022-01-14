package club.mineplex.bot.util;

import club.mineplex.bot.MineplexBot;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;

@UtilityClass
public class UtilFile {

    public File getAppResource(final String path) {
        final File file = new File(MineplexBot.getConfigFolder(), path);
        if (!file.exists()) {
            try {
                if (path.endsWith("/")) {
                    file.mkdir();
                } else {
                    file.createNewFile();
                }
            } catch (final IOException e) {
                MineplexBot.getLogger().error("Could not create file '{}'", file.getName());
                e.printStackTrace();
            }
        }
        return file;
    }

}
