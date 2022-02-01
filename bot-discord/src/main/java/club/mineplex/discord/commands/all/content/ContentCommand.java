package club.mineplex.discord.commands.all.content;

import club.mineplex.discord.BotUtil;
import club.mineplex.discord.Main;
import club.mineplex.discord.commands.SlashCommand;
import club.mineplex.discord.objects.DiscordUser;
import club.mineplex.discord.objects.PermissionHandler;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.net.URL;
import java.util.Objects;
import java.util.regex.Pattern;

public class ContentCommand extends SlashCommand {

    private static final Pattern TWITCH_URL =
            Pattern.compile("(?:http|https)://(?:\\w+.)?twitch\\.(?:tv|com)(?:/([^\\s]+))?");


    private static final Pattern YOUTUBE_URL =
            Pattern.compile(
                    "(?:http(?:s)?://)?(?:www\\.)?(?:youtu\\.be/|youtube\\.com/(?:(?:watch)?\\?(?:.*&)?v(?:i)?=|(?:embed|v|vi|user)/))([^?&\\\"'<> #]+)");

    public ContentCommand() {
        super("content",
              new OptionData(OptionType.STRING, "url", "A valid link to the content you wish to showcase", true)
        );
    }

    @SneakyThrows
    @Override
    protected InteractionHook run(final DiscordUser sender, final SlashCommandEvent commandEvent) {

        final String url = Objects.requireNonNull(commandEvent.getOption("url")).getAsString();

        ContentType contentType = null;
        for (final ContentType value : ContentType.values()) {
            if (value.getPattern().matcher(url).matches()) {
                contentType = value;
                break;
            }
        }

        if (contentType == null) {
            return this.error(commandEvent,
                              "The URL provided is not a valid content link, please contact a bot administrator if you believe this is a mistake."
            );
        }

        new Content.Review(sender, new URL(url), contentType).post();
        return commandEvent
                .reply(BotUtil.getEmojiAsMention(Main.yesEmoji) + " **Your content has been submitted for review!**")
                .setEphemeral(true)
                .complete();
    }

    @Override
    public void onGuildMessageReactionAdd(final GuildMessageReactionAddEvent e) {
        if (e.getMember().getUser().isBot() || !new DiscordUser(e.getMember().getUser()).hasPermissionInGuild(
                Main.HOME_GUILD, PermissionHandler.PermissionLevel.STAFF)) {
            return;
        }

        final long emoteId = e.getReactionEmote().getIdLong();
        ReviewResult result = null;
        if (emoteId == Main.yesEmoji) {
            result = ReviewResult.ACCEPTED;
        } else if (emoteId == Main.neutralEmoji) {
            result = ReviewResult.EXPLICIT;
        } else if (emoteId == Main.noEmoji) {
            result = ReviewResult.REJECTED;
        }

        if (result == null) {
            return;
        }

        final ReviewResult finalResult = result;
        e.getChannel().retrieveMessageById(e.getMessageIdLong()).queue(message -> {
            try {
                Content.Review.process(message, e.getMember().getUser(), finalResult);
            } catch (final IllegalArgumentException ignored) {

            }
        });
    }

}
