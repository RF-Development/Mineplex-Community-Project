package club.mineplex.api.mineplex.mods.clans.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Feature {
    private final String name;
    private final boolean staffOnly;
    private final boolean enabled;

    public static Feature ofPublic(final String name) {
        return new Feature(name, false, true);
    }
}
