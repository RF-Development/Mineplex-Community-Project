package club.mineplex.discord.commands.all;

import club.mineplex.discord.BotUtil;
import club.mineplex.discord.Main;
import club.mineplex.discord.commands.SlashCommand;
import club.mineplex.discord.objects.DiscordUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.Optional;

public class ServerCommand extends SlashCommand {

    public ServerCommand() {
        super("server");
    }

    @Override
    protected InteractionHook run(final DiscordUser sender, final SlashCommandEvent commandEvent) {

        final Optional<Guild> guildOpt = Optional.ofNullable(commandEvent.getGuild());
        if (!guildOpt.isPresent()) {
            return this.error(commandEvent);
        }

        /* MEMBER INFORMATION */
        final Guild guild = guildOpt.get();
        final String memberInfo = String.format(
                "%s [%s users | %s bots]\n" + BotUtil.getEmojiAsMention(656613916004319244L) + " %s" +
                        BotUtil.getEmojiAsMention(656613915991736378L) + BotUtil.getEmojiAsMention(656613916037873664L)
                        + " %s",
                guild.getMemberCache().size(),
                guild.getMemberCache().stream().filter((m) -> !m.getUser().isBot()).count(),
                guild.getMemberCache().stream().filter((m) -> m.getUser().isBot()).count(),
                guild.getMemberCache().stream().filter((m) -> m.getOnlineStatus() != OnlineStatus.OFFLINE &&
                        m.getOnlineStatus() != OnlineStatus.INVISIBLE).count(),
                guild.getMemberCache().stream().filter((m) -> m.getOnlineStatus() == OnlineStatus.OFFLINE ||
                        m.getOnlineStatus() == OnlineStatus.INVISIBLE).count()
        );

        /* CHANNEL INFORMATION */
        final String channelInfo = String.format(
                "%s [%s text | %s voice]",
                guild.getVoiceChannelCache().size() + guild.getTextChannelCache().size(),
                guild.getTextChannelCache().size(),
                guild.getVoiceChannelCache().size()
        );

        /* Building embed and sending it */
        final EmbedBuilder eb = new EmbedBuilder()
                .setTitle(guild.getName())
                .setColor(Main.COLOR_WHITE)
                .setThumbnail(
                        guild.getIconUrl() != null ? guild.getIconUrl() : Main.getJDA().getSelfUser().getAvatarUrl())
                .addField("Owner", guild.getOwner() != null ? guild.getOwner().getAsMention() + "" : "", true)
                .addField("Server Boost",
                          BotUtil.getEmojiAsMention(Main.boostEmoji) + " " + guild.getBoostCount() + " Boosts", true
                )
                .addField("Users", memberInfo, false)
                .addField("Channels", channelInfo, false)
                .addField("Roles", guild.getRoleCache().size() + "", false)
                .addField("Ban Count", guild.retrieveBanList().complete().size() + "", false)
                .addField("Created", BotUtil.formatDate(guild.getTimeCreated()), false);

        return commandEvent.replyEmbeds(eb.build()).complete();
    }

}
