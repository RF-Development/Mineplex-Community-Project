package club.mineplex.discord.commands.all;

import club.mineplex.discord.BotUtil;
import club.mineplex.discord.Main;
import club.mineplex.discord.commands.SlashCommand;
import club.mineplex.discord.objects.DiscordUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Objects;

public class BugReportCommand extends SlashCommand {

    public BugReportCommand() {
        super("bugreport",
              new OptionData(OptionType.STRING, "username", "Your Minecraft username", true),
              new OptionData(OptionType.STRING, "bug", "A detailed description of the bug", true),
              new OptionData(OptionType.STRING, "reproduction", "How do you reproduce this bug?", true),
              new OptionData(OptionType.STRING, "server", "What Mineplex lobby were you in? (ex. Clans-9)", true),
              new OptionData(OptionType.STRING, "version", "Your Minecraft version (ex. 1.8.9)", true),
              new OptionData(OptionType.STRING, "mods", "Did you have any mods? If so, which", true)
        );
    }

    @Override
    protected InteractionHook run(final DiscordUser sender, final SlashCommandEvent commandEvent) {

        final String username = Objects.requireNonNull(commandEvent.getOption("username")).getAsString();
        final String bug = Objects.requireNonNull(commandEvent.getOption("bug")).getAsString();
        final String reproduction = Objects.requireNonNull(commandEvent.getOption("reproduction")).getAsString();
        final String server = Objects.requireNonNull(commandEvent.getOption("server")).getAsString();
        final String version = Objects.requireNonNull(commandEvent.getOption("version")).getAsString();
        final String mods = Objects.requireNonNull(commandEvent.getOption("mods")).getAsString();

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Main.COLOR_GREEN);
        embedBuilder.setAuthor(sender.getUser().getName() + "#" + sender.getUser().getDiscriminator(), null,
                               sender.getUser().getAvatarUrl()
        );
        embedBuilder.appendDescription(String.format("Username: `%s`\n", username));
        embedBuilder.appendDescription(String.format("Bug: `%s`\n", bug));
        embedBuilder.appendDescription(String.format("Reproduction: `%s`\n", reproduction));
        embedBuilder.appendDescription(String.format("Server: `%s`\n", server));
        embedBuilder.appendDescription(String.format("Version: `%s`\n", version));
        embedBuilder.appendDescription(String.format("Mods: `%s`", mods));

        final TextChannel bugReportsChannel = Main.HOME_GUILD.getTextChannelById(Main.bugReportsChannel);
        if (bugReportsChannel == null) {
            return this.error(commandEvent);
        }

        bugReportsChannel.sendMessageEmbeds(embedBuilder.build()).queue();
        return commandEvent.reply(BotUtil.getEmojiAsMention(Main.yesEmoji) + " **Successfully submitted bug report!**")
                           .setEphemeral(true).complete();
    }

}
