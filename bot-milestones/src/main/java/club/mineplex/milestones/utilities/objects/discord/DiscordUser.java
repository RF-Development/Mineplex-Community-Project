package club.mineplex.milestones.utilities.objects.discord;

import club.mineplex.milestones.utilities.objects.permissibles.PermissionHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public class DiscordUser {

    private final User user;

    public DiscordUser(final User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public boolean isInGuild(final Guild guild) {
        return guild.getMemberById(this.getUser().getIdLong()) != null;
    }

    public boolean hasRoleInGuild(final String roleId, final Guild guild) {
        final Role toCheck = guild.getRoleById(roleId);
        if (toCheck == null) {
            return false;
        }
        for (final Member m : guild.getMembersWithRoles(toCheck)) {
            if (m.getIdLong() == this.user.getIdLong()) {
                return true;
            }
        }
        return false;
    }

    public Member getAsMember(final Guild guild) {
        return this.isInGuild(guild) ? guild.getMember(this.getUser()) : null;
    }

    public boolean hasPermissionInGuild(final Guild guild, final PermissionHandler.PermissionLevel permissionLevel) {
        return PermissionHandler.getPermissionLevel(this, guild).power >= permissionLevel.power;
    }

    public boolean isAdminUser() {
        return PermissionHandler.isAdminUser(this);
    }

}
