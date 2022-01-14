package club.mineplex.discord.commands.events.christmas;

import club.mineplex.discord.Main;
import club.mineplex.discord.commands.IShop;
import club.mineplex.discord.commands.SlashCommand;
import club.mineplex.discord.objects.DiscordUser;
import club.mineplex.discord.objects.PermissionHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.Optional;

public class RestartTriviaCommand extends SlashCommand implements IShop.Permissible {

    public RestartTriviaCommand() {
        super("restarttrivia");
        this.setBypassChannels(true);
    }

    @Override
    protected InteractionHook run(final DiscordUser sender, final SlashCommandEvent commandEvent) {

        final Optional<Guild> guildOpt = Optional.ofNullable(commandEvent.getGuild());
        if (!guildOpt.isPresent()) {
            return this.error(commandEvent);
        }

        final Guild guild = guildOpt.get();
        final TextChannel sendChannel = guild.getTextChannelsByName(Main.christmasTriviaChannel, true).get(0);
        if (sendChannel == null) {
            return this.error(commandEvent);
        }

        sendChannel.createCopy()
                   .queue(channel -> channel.sendMessage("`Registered answers will be posted in this channel.`")
                                            .queue());
        sendChannel.delete().queue();

        for (final SlashCommand slashCommand : Main.getCommands()) {
            if (!(slashCommand instanceof AnswerCommand)) {
                continue;
            }

            ((AnswerCommand) slashCommand).alreadyAnswered.clear();
        }

        return commandEvent.reply("**Successfully restarted trivia!**").complete();
    }

    @Override
    public boolean hasPermission(final DiscordUser user, final Guild guild) {
        return user.hasPermissionInGuild(guild, PermissionHandler.PermissionLevel.ADMIN);
    }

}
