package club.mineplex.api.mineplex.website.profile.models;

import club.mineplex.api.mineplex.website.utilities.WebsiteUtilities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileAvatar {
    private final AvatarType type;
    @Nullable
    @JsonIgnore
    private final String gravatarId;
    @JsonIgnore
    private final int profileId;

    public static ProfileAvatar ofCustom(final int profileId) {
        return new ProfileAvatar(
                AvatarType.CUSTOM,
                null,
                profileId
        );
    }

    public static ProfileAvatar ofDefault() {
        return new ProfileAvatar(
                AvatarType.DEFAULT,
                null,
                -1
        );
    }

    public static ProfileAvatar ofGravatar(final String gravatarId) {
        return new ProfileAvatar(
                AvatarType.GRAVATAR,
                gravatarId,
                -1
        );
    }

    private int getAvatarSavePosition() {
        return this.profileId / 1_000;
    }

    public String getAvatarUrl() {
        switch (this.type) {
            case CUSTOM:
                return WebsiteUtilities.getBaseUrl() + "/data/avatars/s/" + this.getAvatarSavePosition() + "/" + this.profileId + ".jpg";
            case DEFAULT:
                return WebsiteUtilities.getBaseUrl() + "/styles/default/xenforo/avatars/avatar_s.png";
            case GRAVATAR:
                return "https://secure.gravatar.com/avatar/" + this.gravatarId;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public Optional<String> getGravatarId() {
        return Optional.ofNullable(this.gravatarId);
    }

    public Optional<Integer> getProfileId() {
        if (this.profileId == -1) {
            return Optional.empty();
        }

        return Optional.of(this.profileId);
    }
}
