package club.mineplex.discord.events;

import club.mineplex.discord.Main;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Filter extends ListenerAdapter {

    private static List<String> filter = new ArrayList<>(Arrays.asList(Main.filter.getLines()));

    public static void updateFilter() {
        if (Main.FILTER_ENABLED) {
            filter = new ArrayList<>(Arrays.asList(Main.filter.getLines()));
        } else {
            filter = Collections.emptyList();
        }
    }

    @Override
    public void onGuildMessageReceived(final GuildMessageReceivedEvent e) {
        if (!Main.FILTER_ENABLED) {
            return;
        }
        if (e.getAuthor().isBot() || e.getMessage().getChannelType() != ChannelType.TEXT) {
            return;
        }

        final Message msg = e.getMessage();
        final String raw = e.getMessage().getContentRaw();

        if (this.checkForFilter(raw)) {
            msg.delete().queue();
        }
    }

    private boolean checkForFilter(final String text) {
        final String[] words = text.split(" ");

        for (final String badWord : filter) {
            for (final String word : words) {
                if (word.replaceAll("[^a-zA-Z]", "").equalsIgnoreCase(badWord)) {
                    return true;
                }
            }
        }

        return false;
    }

}
