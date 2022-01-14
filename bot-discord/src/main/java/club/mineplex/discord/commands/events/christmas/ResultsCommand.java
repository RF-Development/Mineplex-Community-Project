package club.mineplex.discord.commands.events.christmas;

import club.mineplex.discord.Main;
import club.mineplex.discord.commands.IShop;
import club.mineplex.discord.commands.SlashCommand;
import club.mineplex.discord.objects.DiscordUser;
import club.mineplex.discord.objects.PermissionHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ResultsCommand extends SlashCommand implements IShop.Permissible {

    public ResultsCommand() {
        super("results");
        this.setBypassChannels(true);
    }

    @Override
    protected InteractionHook run(final DiscordUser sender, final SlashCommandEvent commandEvent) {

        final File dumpFolder = new File("dump");
        dumpFolder.mkdir();

        for (final SlashCommand slashCommand : Main.getCommands()) {
            if (!(slashCommand instanceof VoteCommand)) {
                continue;
            }

            final VoteCommand command = (VoteCommand) slashCommand;

            final StringBuilder stringBuilder = new StringBuilder();
            for (final String award : command.voted.keySet()) {
                stringBuilder.append("========================").append("\n");
                stringBuilder.append(award).append("\n");
                stringBuilder.append("========================").append("\n");

                final List<String> collect = command.voted.get(award).values().stream().map(String::toLowerCase)
                                                          .collect(Collectors.toList());
                final HashMap<String, Integer> votes = new HashMap<>();
                for (final String vote : collect) {
                    if (!votes.containsKey(vote)) {
                        votes.put(vote, 1);
                    } else {
                        votes.replace(vote, votes.get(vote) + 1);
                    }
                }

                final List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(votes.entrySet());
                list.sort((m1, m2) -> m2.getValue().compareTo(m1.getValue()));

                for (final Map.Entry<String, Integer> entry : list) {
                    stringBuilder.append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
                }

                stringBuilder.append("\n").append("\n").append("\n");
            }

            File fileToSave = null;
            try {
                fileToSave = new File(dumpFolder, "award_votes_" + UUID.randomUUID().toString() + ".txt");
                fileToSave.createNewFile();

                final FileWriter myWriter = new FileWriter(fileToSave);
                myWriter.write(stringBuilder.toString().trim());
                myWriter.close();
            } catch (final IOException e) {
                e.printStackTrace();
                return commandEvent.reply("**Err.. Something went wrong?**").complete();
            }

            return commandEvent.deferReply().addFile(fileToSave).complete();
        }

        return commandEvent.reply("**Err.. Something went wrong?**").complete();
    }

    @Override
    public boolean hasPermission(final DiscordUser user, final Guild guild) {
        return user.hasPermissionInGuild(guild, PermissionHandler.PermissionLevel.ADMIN);
    }

}
