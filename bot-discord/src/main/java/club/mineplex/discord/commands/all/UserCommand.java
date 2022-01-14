package club.mineplex.discord.commands.all;

import club.mineplex.discord.BotUtil;
import club.mineplex.discord.Main;
import club.mineplex.discord.commands.SlashCommand;
import club.mineplex.discord.objects.DiscordUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

public class UserCommand extends SlashCommand {

    public UserCommand() {
        super("user", new OptionData(OptionType.USER, "user", "The user whose information you wish to retrieve"));
    }

    @Override
    protected InteractionHook run(final DiscordUser sender, final SlashCommandEvent commandEvent) {

        final Guild guild = commandEvent.getGuild();
        Member toDisplay = sender.getAsMember(guild);
        final Optional<OptionMapping> userOpt = Optional.ofNullable(commandEvent.getOption("user"));

        if (userOpt.isPresent()) {
            final OptionMapping userObj = userOpt.get();
            toDisplay = userObj.getAsMember();
        }

        /* Tagged user is invalid */
        if (toDisplay == null) {
            return this.error(commandEvent, "User to display is invalid!");
        }

        final String status = String.format(
                "%s %s",
                BotUtil.getEmojiAsMention(
                        toDisplay.getOnlineStatus() != OnlineStatus.INVISIBLE && toDisplay.getOnlineStatus() !=
                                OnlineStatus.OFFLINE ? 656613916004319244L : 656613916037873664L),
                toDisplay.getOnlineStatus() != OnlineStatus.INVISIBLE &&
                        toDisplay.getOnlineStatus() != OnlineStatus.OFFLINE ? "Online" : "Offline"
        );

        final String highestRole = toDisplay.getRoles().isEmpty() ? "@everyone" : toDisplay.getRoles().get(0).getAsMention();
        final EmbedBuilder eb = new EmbedBuilder()
                .setAuthor(toDisplay.getUser().getName() + "#" + toDisplay.getUser().getDiscriminator(), null,
                           toDisplay.getUser().getAvatarUrl()
                )
                .setThumbnail(toDisplay.getUser().getAvatarUrl())
                .setColor(Main.COLOR_WHITE)
                .addField("Username", toDisplay.getUser().getName(), true)
                .addField("Nickname", toDisplay.getNickname() == null ? "None" : toDisplay.getNickname(), true)
                .addField("Profile", toDisplay.getAsMention(), true)
                .addField("Status", status, true)
                .addField("Boosts Server ", toDisplay.getTimeBoosted() != null ? BotUtil.getEmojiAsMention(
                        Main.yesEmoji) : BotUtil.getEmojiAsMention(Main.noEmoji), true)
                .addField("Highest Role", highestRole, true)
                .addField("Joined Discord", BotUtil.formatDate(toDisplay.getTimeCreated()), true)
                .addField("Joined Server", BotUtil.formatDate(toDisplay.getTimeJoined()), true);

        return commandEvent.replyEmbeds(eb.build()).complete();
    }

}
