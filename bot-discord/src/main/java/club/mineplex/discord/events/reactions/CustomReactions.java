package club.mineplex.discord.events.reactions;

import club.mineplex.discord.objects.DiscordUser;
import club.mineplex.discord.objects.PermissionHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.List;

/*
 * CUSTOM REACTIONS
 * Current:
 * - :warning: | Removes all reactions in one message in suggestions channel
 */
public class CustomReactions extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(final GuildMessageReactionAddEvent e) {
        try {
            if (e.getReactionEmote().getName().equalsIgnoreCase("⚠️")) {
                final RestAction<List<Webhook>> ra = e.getGuild().retrieveWebhooks();
                final List<Webhook> webhooks = ra.complete();

                /* Looping through all webhooks to see if suggestions is a thing */
                for (final Webhook w : webhooks) {

                    /* Is in suggestions channel */
                    if (w.getName().equals("Suggestions") && e.getChannel().equals(w.getChannel())) {

                        /* Checking if they have permission to use*/
                        if (!new DiscordUser(e.getMember().getUser())
                                .hasPermissionInGuild(e.getGuild(), PermissionHandler.PermissionLevel.STAFF)) {
                            return;
                        }

                        final Message query = w.getChannel().retrieveMessageById(e.getMessageIdLong()).complete();
                        query.clearReactions().queue();
                    }

                }
            }
        } catch (final InsufficientPermissionException ignored) {

        }
    }

}
