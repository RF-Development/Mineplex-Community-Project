package club.mineplex.discord.commands.events.christmas;

import club.mineplex.discord.BotUtil;
import club.mineplex.discord.Main;
import club.mineplex.discord.commands.SlashCommand;
import club.mineplex.discord.objects.DiscordUser;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class VoteCommand extends SlashCommand {

    HashMap<String, HashMap<Long, String>> voted = new HashMap<>();

    public VoteCommand() {
        super("vote",
              new OptionData(OptionType.STRING, "category", "The award category to vote for this nominee",
                             true
              ).addChoices(getChoices()),
              new OptionData(OptionType.STRING, "nominee", "The username of the nominee", true)
        );

        for (final Command.Choice choice : getChoices()) {
            this.voted.put(choice.getAsString(), new HashMap<>());
            this.updateFromFile(choice.getAsString());
        }


        this.setBypassChannels(true);
    }

    private static Command.Choice[] getChoices() {
        final List<String> questions = Main.christmas.getOrDefault("questions", List.class, new ArrayList<>());

        final List<Command.Choice> choices = new ArrayList<>();

        for (final String name : questions) {
            final Command.Choice choice = new Command.Choice(name, name);
            choices.add(choice);
        }

        return choices.toArray(new Command.Choice[0]);
    }

    @Override
    protected InteractionHook run(final DiscordUser sender, final SlashCommandEvent commandEvent) {

        final Optional<OptionMapping> catOpt = Optional.ofNullable(commandEvent.getOption("category"));
        final Optional<OptionMapping> nameOpt = Optional.ofNullable(commandEvent.getOption("nominee"));

        if (!nameOpt.isPresent() || !catOpt.isPresent()) {
            return null;
        }

        final String category = catOpt.get().getAsString();
        final String name = nameOpt.get().getAsString();

        if (!this.voted.containsKey(category)) {
            return this.error(commandEvent, "**That category is invalid!**");
        }

        if (this.voted.get(category).containsKey(sender.getUser().getIdLong())) {
            return this.error(commandEvent, "**You have already voted for this category!**");
        }


        try {
            File fileToSave = null;
            final File dumpFolder = new File("dump");
            dumpFolder.mkdir();

            fileToSave = new File(dumpFolder, category + ".txt");
            fileToSave.createNewFile();

            final FileInputStream inputStream = new FileInputStream(fileToSave);
            final String everything = IOUtils.toString(inputStream);

            final StringBuilder stringBuilder = new StringBuilder(everything);
            stringBuilder.append("\n").append(name).append(":").append(sender.getUser().getIdLong());

            final FileWriter myWriter = new FileWriter(fileToSave);
            myWriter.write(stringBuilder.toString().trim());
            myWriter.close();

            this.updateFromFile(category);

        } catch (final IOException e) {
            e.printStackTrace();
            return commandEvent.reply("**Err.. Something went wrong?**").complete();
        }

        return commandEvent.reply(String.format("**Successfully voted for `%s`!** %s", name,
                                                BotUtil.getEmoji(Main.yesEmoji).getAsMention()
        )).setEphemeral(true).complete();
    }

    public void updateFromFile(final String category) {

        try {
            final File dumpFolder = new File("dump");
            dumpFolder.mkdir();

            final File fileToSave = new File(dumpFolder, category + ".txt");
            fileToSave.createNewFile();

            final FileInputStream inputStream = new FileInputStream(fileToSave);
            final String everything = IOUtils.toString(inputStream);

            final String[] split = everything.split("\n");
            for (final String line : split) {
                try {
                    final long id = Long.parseLong(line.substring(line.lastIndexOf(":") + 1));
                    final String name = line.substring(0, line.lastIndexOf(":"));

                    this.voted.get(category).put(id, name);
                } catch (final StringIndexOutOfBoundsException | NumberFormatException e) {
                    continue;
                }
            }

        } catch (final IOException e) {
            e.printStackTrace();
        }

    }

}
