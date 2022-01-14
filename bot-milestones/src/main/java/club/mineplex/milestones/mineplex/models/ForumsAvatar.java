package club.mineplex.milestones.mineplex.models;

import java.util.Locale;

public class ForumsAvatar {

    public final String avatarURL;
    public final AvatarType avatarType;
    public ForumsAvatar(final String avatarURL, final String avatarType) {
        this.avatarURL = avatarURL;
        this.avatarType = AvatarType.fromLabel(avatarType);
    }

    public enum AvatarType {

        GRAVATAR("gravatar"),
        DEFAULT("default"),
        CUSTOM("custom");

        String label;

        AvatarType(final String label) {
            this.label = label;
        }

        public static AvatarType fromLabel(final String label) {

            for (final AvatarType type : AvatarType.values()) {

                if (label.toLowerCase(Locale.ROOT).equals(type.label.toLowerCase(Locale.ROOT))) {
                    return type;
                }

            }

            return null;
        }

    }
}
