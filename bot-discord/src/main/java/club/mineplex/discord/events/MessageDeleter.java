package club.mineplex.discord.events;

import club.mineplex.discord.Main;
import club.mineplex.discord.commands.SlashCommand;
import club.mineplex.discord.commands.all.BugReportCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

/*
 * USED TO DELETE OTHER BOT'S HELP MESSAGES THAT ARE INCOMPATIBLE WITH OURS
 * DELETES BOT COMMANDS PASTED IN THE COMMANDS CHANNEL IF SOMEONE MISSPELLS IT
 */
public class MessageDeleter extends ListenerAdapter {

    @Override
    public void onMessageReceived(final MessageReceivedEvent e) {
        /* If author isn't a bot, skip */
        if (e.getAuthor().isBot()) {
            return;
        }

        /* Raw message content */
        final String message = e.getMessage().getContentRaw();
        if (message.length() < 2) {
            return;
        }

        for (final SlashCommand command : Main.getCommands()) {
            if (!(command instanceof BugReportCommand)) {
                continue;
            }

            final String commandText = message.substring(1).toLowerCase();
            final String commandName = command.getCommandData().getName().toLowerCase();
            if (commandText.startsWith(commandName)) {
                e.getMessage().getTextChannel().sendMessage(
                        ":x: **Hey " + e.getAuthor().getAsMention() + ", I have deleted your message " +
                                "as it didn't execute the command properly. Please use the `/" + commandName
                                + "` slash command!**"
                ).completeAfter(1L, TimeUnit.SECONDS).delete().queueAfter(5L, TimeUnit.SECONDS);

                try {
                    e.getMessage().delete().queueAfter(1L, TimeUnit.SECONDS);
                } catch (final ErrorResponseException ex) {
                    if (!ex.getMessage().contains("Unknown Message")) {
                        ex.printStackTrace();
                    }
                }

                return;
            }
        }

    }
}
