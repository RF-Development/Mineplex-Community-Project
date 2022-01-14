package club.mineplex.api.mineplex.website.profile.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
public class JavaPlayer extends Player {
    private final UUID uuid;

    public JavaPlayer(final Rank rank, final String name, final UUID uuid) {
        super(rank, name);

        this.uuid = uuid;
    }
}
