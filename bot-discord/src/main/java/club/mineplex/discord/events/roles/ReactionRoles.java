package club.mineplex.discord.events.roles;

import club.mineplex.discord.Main;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

/*
    CUSTOM REACTION ROLES
 */
public class ReactionRoles extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(final MessageReactionAddEvent e) {
        /* Usual conditions check */
        if (e.getChannel().getType() != ChannelType.TEXT) {
            return;
        }
        if (e.getMember() == null) {
            return;
        }

        /* Getting reaction roles */
        final List<String> in = Main.config.getOrDefault("roles", ArrayList.class, new ArrayList<>());

        for (final String s : in) {
            final String[] t = s.split(":");
            final String messageId = t[1];
            final String roleId = t[0];
            final String emojiName = t[2];

            if (messageId.equalsIgnoreCase(e.getMessageId()) && e.getReactionEmote().getName().equals(emojiName)) {
                if (e.getGuild().getRoles().isEmpty()) {
                    continue;
                }
                if (e.getGuild().getRoleById(roleId) == null) {
                    return;
                }

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

    @Override
    public void onMessageReactionRemove(final MessageReactionRemoveEvent e) {
        /* Usual condition checks */
        if (e.getChannel().getType() != ChannelType.TEXT) {
            return;
        }
        if (e.getMember() == null) {
            return;
        }

        /* Getting reaction roles */
        final List<String> in = Main.config.getOrDefault("roles", ArrayList.class, new ArrayList<>());

        for (final String s : in) {
            final String[] text = s.split(":");
            final String messageId = text[1];
            final String roleId = text[0];
            final String emojiName = text[2];

            if (messageId.equalsIgnoreCase(e.getMessageId()) && e.getReactionEmote().getName().equals(emojiName)) {
                if (e.getGuild().getRoles().isEmpty()) {
                    continue;
                }
                if (e.getGuild().getRoleById(roleId) == null) {
                    return;
                }

                try {
                    final Role role = e.getGuild().getRoleById(roleId);
                    if (role == null || !e.getMember().getRoles().contains(role)) {
                        return;
                    }
                    e.getGuild().removeRoleFromMember(e.getMember(), role).queue();
                } catch (final Exception exception) {
                    exception.printStackTrace();
                }

            }
        }
    }

}
