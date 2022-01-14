package club.mineplex.discord.commands.all;

import club.mineplex.discord.BotUtil;
import club.mineplex.discord.commands.SlashCommand;
import club.mineplex.discord.objects.DiscordUser;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class PingCommand extends SlashCommand {

    public PingCommand() {
        super("ping");
    }

    @Override
    protected InteractionHook run(final DiscordUser sender, final SlashCommandEvent commandEvent) {

        final Optional<Guild> guildOpt = Optional.ofNullable(commandEvent.getGuild());
        if (!guildOpt.isPresent()) {
            return this.error(commandEvent);
        }
        final Guild guild = guildOpt.get();

        final InteractionHook hook = commandEvent.reply(
                BotUtil.getEmojiAsMention(666724589300350986L) + " **Bot's Ping** :mag_right: ``Loading...``"
        ).complete();

        hook.editOriginal(String.format(BotUtil.getEmojiAsMention(656613916004319244L) +
                                                " **Bot's Ping** :mag_right: ``%s ms``",
                                        guild.getJDA().getGatewayPing()
            ))
            .queueAfter(3, TimeUnit.SECONDS);

        return hook;
    }

}
