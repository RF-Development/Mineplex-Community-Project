package club.mineplex.discord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class TextFile {

    private final String fileName;
    private File file = null;

    public TextFile(final String file) {
        this.fileName = file;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String[] getLines() {
        if (this.file == null || !this.file.exists()) {
            return new String[0];
        }

        try {
            final List<String> lines = Files.readAllLines(this.file.toPath());
            final String[] toReturn = new String[lines.size()];
            for (int i = 0; i < lines.size(); i++) {
                toReturn[i] = lines.get(i);
            }
            return toReturn;
        } catch (final IOException e) {
            return new String[0];
        }
    }

    public boolean exists() {
        return this.file != null && this.file.exists();
    }

    public boolean load() {
        this.file = new File(this.fileName);
        return this.file.exists();
    }

}
