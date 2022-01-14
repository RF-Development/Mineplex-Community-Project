package club.mineplex.api.mineplex.website.utilities;

import club.mineplex.api.mineplex.website.profile.models.ProfileAvatar;
import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class AvatarUtilities {
    private static final Pattern GRAVATAR_AVATAR = Pattern.compile("https:\\/\\/secure\\.gravatar\\.com\\/avatar\\/(.*)\\?");

    public boolean isDefaultAvatar(final String avatarUrl) {
        return avatarUrl.contains("/styles/default/xenforo/avatars/");
    }

    public Optional<String> getGravatarAvatarId(final String avatarUrl) {
        final Matcher matcher = GRAVATAR_AVATAR.matcher(avatarUrl);
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        }

        return Optional.empty();
    }

    public ProfileAvatar getProfileAvatar(final String avatarUrl, final int profileId) {
        if (AvatarUtilities.isDefaultAvatar(avatarUrl)) {
            return ProfileAvatar.ofDefault();
        }

        final Optional<String> gravatarIdOpt = AvatarUtilities.getGravatarAvatarId(avatarUrl);
        if (gravatarIdOpt.isPresent()) {
            return ProfileAvatar.ofGravatar(gravatarIdOpt.get());
        }

        return ProfileAvatar.ofCustom(profileId);
    }
}
