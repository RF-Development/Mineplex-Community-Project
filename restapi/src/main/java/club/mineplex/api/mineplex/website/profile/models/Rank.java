package club.mineplex.api.mineplex.website.profile.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
@Getter
public enum Rank {
    /* STAFF */
    OWNER(ServerPlatform.GLOBAL, "Owner"),
    DEVELOPER(ServerPlatform.GLOBAL, "Dev"),
    ADMIN(ServerPlatform.GLOBAL, "Admin"),
    SUPPORT(ServerPlatform.GLOBAL, "Support"),
    SENIOR_MOD(ServerPlatform.GLOBAL, "Sr.Mod"),
    MAP_LEAD(ServerPlatform.GLOBAL, "Map Lead"),
    BUILDER(ServerPlatform.GLOBAL, "Builder"),
    MOD(ServerPlatform.GLOBAL, "Mod"),
    TRAINEE(ServerPlatform.GLOBAL, "Trainee"),

    /* PAID JAVA */
    IMMORTAL(ServerPlatform.JAVA, "Immortal"),
    ETERNAL(ServerPlatform.JAVA, "Eternal"),
    TITAN(ServerPlatform.JAVA, "Titan"),
    LEGEND(ServerPlatform.JAVA, "Legend"),
    HERO(ServerPlatform.JAVA, "Hero"),
    ULTRA(ServerPlatform.JAVA, "Ultra"),

    /* PAID BEDROCK */
    LORD(ServerPlatform.BEDROCK, "Lord"),
    LADY(ServerPlatform.BEDROCK, "Lady"),
    KNIGHT(ServerPlatform.BEDROCK, "Knight"),
    DUKE(ServerPlatform.BEDROCK, "Duke"),
    DUCHESS(ServerPlatform.BEDROCK, "Duchess"),

    /* OTHER */
    UNKOWN(ServerPlatform.GLOBAL, "Hidden"),
    MEMBER(ServerPlatform.GLOBAL, "Member");

    private final ServerPlatform serverType;
    private final String name;

    public static Optional<Rank> getRankByName(final String name) {
        for (final Rank rank : Rank.values()) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return Optional.of(rank);
            }
        }
        return Optional.empty();
    }
}
