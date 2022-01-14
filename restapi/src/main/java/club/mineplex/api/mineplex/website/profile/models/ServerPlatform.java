package club.mineplex.api.mineplex.website.profile.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServerPlatform {
    GLOBAL("Global"),
    BEDROCK("Bedrock"),
    JAVA("Java");

    private final String name;
}
