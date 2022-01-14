package club.mineplex.api.mineplex.website.profile.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EntireProfile extends Profile {
    @Nullable
    private final BedrockPlayer bedrockPlayer;
    @Nullable
    private final JavaPlayer javaPlayer;
    private final ForumStatistic forumStatistic;

    public EntireProfile(final int memberId,
                         final ProfileAvatar profileAvatar,
                         final String name,
                         @Nullable final BedrockPlayer bedrockPlayer,
                         @Nullable final JavaPlayer javaPlayer,
                         final ForumStatistic forumStatistic) {
        super(memberId, profileAvatar, name);
        this.bedrockPlayer = bedrockPlayer;
        this.javaPlayer = javaPlayer;
        this.forumStatistic = forumStatistic;
    }

    public Optional<BedrockPlayer> getBedrockPlayer() {
        return Optional.ofNullable(this.bedrockPlayer);
    }

    public Optional<JavaPlayer> getJavaPlayer() {
        return Optional.ofNullable(this.javaPlayer);
    }
}
