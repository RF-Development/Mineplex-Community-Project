package club.mineplex.discord.events;

import club.mineplex.discord.Main;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;

public class AutoResponses extends ListenerAdapter {

    @Override
    public void onMessageReceived(final MessageReceivedEvent e) {

        /* If author is bot or channel type is not a discord guild text */
        if (e.getAuthor().isBot() || e.getMessage().getChannelType() != ChannelType.TEXT) {
            return;
        }

        /* Initiailizing variables */
        final Message msg = e.getMessage();
        final String raw = msg.getContentRaw();

        /* Looping to see if the message contains any responses */
        final ArrayList<String> responses = (ArrayList<String>)
                Main.config.getOrDefault("responses", ArrayList.class, new ArrayList<>());

        for (final String r : responses) {
            final String[] cue = r.split(";");

            /* Queueing response, LIMITED TO 1 per message */
            if (raw.equalsIgnoreCase(cue[0])) {
                String response = cue[1];

                for (int i = 2; i < cue.length; i++) {
                    response += "\n" + cue[i];
                }

                msg.getTextChannel().sendMessage(response).queue();
                return;
            }

        }
    }

}
