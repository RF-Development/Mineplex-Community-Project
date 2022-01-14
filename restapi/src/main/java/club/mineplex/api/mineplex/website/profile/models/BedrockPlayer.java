package club.mineplex.api.mineplex.website.profile.models;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BedrockPlayer extends Player {
    public BedrockPlayer(final Rank rank, final String name) {
        super(rank, name);
    }
}
