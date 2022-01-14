package club.mineplex.api.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;

@UtilityClass
public class FileUtilities {
    public void saveToFile(final ObjectMapper mapper, final File file, final Object value) throws IOException {
        // Make sure that the directories exist
        file.getParentFile().mkdirs();
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, value);
    }
}
