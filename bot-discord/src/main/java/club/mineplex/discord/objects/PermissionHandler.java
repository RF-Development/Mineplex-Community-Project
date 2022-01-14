package club.mineplex.discord.objects;

import club.mineplex.discord.Main;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;

public class PermissionHandler {

    public static PermissionLevel getPermissionLevel(final DiscordUser user, final Guild guild) {

        PermissionLevel level = PermissionLevel.NONE;
        for (final PermissionLevel value : PermissionLevel.values()) {
            if (value.power <= level.power) {
                continue;
            }
            for (final long role : value.roles) {
                if (user.hasRoleInGuild(Long.toString(role), guild)) {
                    level = value;
                }
            }
        }

        return user.isAdminUser() ? PermissionLevel.ADMIN : level;
    }

    public static boolean isAdminUser(final DiscordUser user) {
        return Main.config.getOrDefault("admin-users",
                                        ArrayList.class, new ArrayList<>()
        ).contains(user.getUser().getIdLong());
    }

    public enum PermissionLevel {
        STAFF(100), ADMIN(200), NONE(0);

        public int power;
        public ArrayList<Long> roles;

        PermissionLevel(final int power) {
            this.power = power;
            this.roles = new ArrayList<>();
        }

        public void setRoles(final ArrayList<Long> roles) {
            this.roles = roles;
        }

    }

}
