package club.mineplex.api.mineplex.mods.clans.models;

import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@Data
public class ReleaseInfo {
    private final String lastVersion;
    private final String changelog;
    @Nullable
    private final String downloadUrl;

    public Optional<String> getDownloadUrl() {
        return Optional.ofNullable(this.downloadUrl);
    }
}
