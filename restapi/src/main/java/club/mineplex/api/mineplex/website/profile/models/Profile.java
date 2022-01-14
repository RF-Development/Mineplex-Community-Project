package club.mineplex.api.mineplex.website.profile.models;

import club.mineplex.api.mineplex.website.utilities.WebsiteUtilities;
import lombok.Data;

@Data
public class Profile {
    private final int memberId;
    private final ProfileAvatar avatar;
    private final String name;

    public String getProfileUrl() {
        return WebsiteUtilities.getBaseUrl() + "/members/" + this.memberId;
    }
}
