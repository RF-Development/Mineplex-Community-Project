package club.mineplex.discord.objects;

import java.net.MalformedURLException;
import java.net.URL;

public class PlayerProfile {

    private final String name;
    private final URL profilePage;

    public PlayerProfile(final String name, final URL profilePage) {
        this.name = name;
        this.profilePage = profilePage;
    }

    public String getName() {
        return this.name;
    }

    public URL getProfilePage() {
        try {
            return this.profilePage == null ? new URL("https://google.com") : this.profilePage;
        } catch (final MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
