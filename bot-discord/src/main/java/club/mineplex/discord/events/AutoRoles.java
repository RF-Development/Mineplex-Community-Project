package club.mineplex.discord.events;

import club.mineplex.discord.Main;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AutoRoles extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(final GuildMemberJoinEvent e) {
        /* Getting roles */
        final List<Long> in = (List<Long>) Main.config.getOrDefault("autoroles", ArrayList.class, new ArrayList<>());

        /* Looping through all the roles to see if we can give to player */
        for (final long roleId : in) {

            /* Returning if the guild has no roles*/
            if (e.getGuild().getRoles().isEmpty()) {
                return;
            }

            /* Continuing in the list if the role doesn't exist*/
            if (e.getGuild().getRoleById(roleId) == null) {
                continue;
            }

            /* Giving the role to the user*/
            try {
                final Role role = e.getGuild().getRoleById(roleId);
                if (role == null || e.getMember().getRoles().contains(role)) {
                    return;
                }
                e.getGuild().addRoleToMember(e.getMember(), role).queue();
            } catch (final Exception exception) {
                exception.printStackTrace();
            }

        }
    }

}
