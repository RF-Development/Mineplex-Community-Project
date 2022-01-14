package club.mineplex.discord.commands.staff.suggestions;

import club.mineplex.discord.BotUtil;
import club.mineplex.discord.Main;
import club.mineplex.discord.commands.IShop;
import club.mineplex.discord.commands.SlashCommand;
import club.mineplex.discord.objects.DiscordUser;
import club.mineplex.discord.objects.PermissionHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.RestAction;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SuggestionsCommand extends SlashCommand implements IShop.Permissible {

    public SuggestionsCommand() {
        super("suggestions");

        this.commandData.addSubcommands(new SubcommandData(
                "enable",
                "Enable the suggestions module"
        ).addOption(OptionType.CHANNEL, "channel", "The channel that suggestions would be sent to", true));

        this.commandData.addSubcommands(new SubcommandData(
                "disable",
                "Disable the suggestions module"
        ));

        this.setBypassChannels(true);
    }

    @Override
    protected InteractionHook run(final DiscordUser sender, final SlashCommandEvent commandEvent) {

        final Optional<Guild> guildOpt = Optional.ofNullable(commandEvent.getGuild());
        if (!guildOpt.isPresent()) {
            return this.error(commandEvent);
        }

        final Guild guild = guildOpt.get();
        final boolean enabled = Objects.requireNonNull(commandEvent.getSubcommandName()).equalsIgnoreCase("enable");

        /* Checking for argument action */
        if (!enabled) {

            final RestAction<List<Webhook>> ra = guild.retrieveWebhooks();
            final List<Webhook> webhooks = ra.complete();

            /* Checking for suggestions webhook */
            for (final Webhook w : webhooks) {

                /* Success, suggestions disabled */
                if (w.getName().equals("Suggestions")) {
                    w.delete().queue();
                    final EmbedBuilder eb = new EmbedBuilder()
                            .setColor(Main.COLOR_GREEN)
                            .setDescription(BotUtil.getEmojiAsMention(Main.yesEmoji)
                                                    + " The suggestions feature has been disabled.");
                    return commandEvent.replyEmbeds(eb.build()).complete();
                }
            }

            /* Error, suggestsions was already disabled */
            final EmbedBuilder eb = new EmbedBuilder()
                    .setColor(Main.COLOR_RED)
                    .setDescription(
                            BotUtil.getEmojiAsMention(Main.COLOR_RED) + " The suggestions feature isn't enabled.");
            return commandEvent.replyEmbeds(eb.build()).complete();

        } else {

            final RestAction<List<Webhook>> ra = guild.retrieveWebhooks();
            final List<Webhook> webhooks = ra.complete();
            final OptionMapping channelOpt = Objects.requireNonNull(commandEvent.getOption("channel"));

            if (channelOpt.getChannelType() != ChannelType.TEXT) {
                return this.error(commandEvent, "The specified channel is not a text channel!");
            }

            /* Looking for suggestions webhook */
            final TextChannel channel = (TextChannel) channelOpt.getAsGuildChannel();
            for (final Webhook w : webhooks) {

                /* Error, Suggestions is already enabled */
                if (w.getName().equals("Suggestions")) {

                    final EmbedBuilder eb = new EmbedBuilder()
                            .setColor(Main.COLOR_RED)
                            .setDescription(
                                    BotUtil.getEmojiAsMention(Main.noEmoji) + " Suggestions are already being " +
                                            "shown in " + w.getChannel().getAsMention()
                                            + ". Please disable this first.");

                    return commandEvent.replyEmbeds(eb.build()).complete();
                }
            }

            final InputStream s = BotUtil.imageFromUrl(Main.getJDA().getSelfUser().getAvatarUrl());
            try {
                channel.createWebhook("Suggestions").setAvatar(Icon.from(s)).queue();
            } catch (final Exception e) {
                channel.createWebhook("Suggestions").queue();
            }

            /* Success, suggestions was enabled */
            final EmbedBuilder eb = new EmbedBuilder()
                    .setColor(Main.COLOR_GREEN)
                    .setDescription(
                            BotUtil.getEmojiAsMention(Main.yesEmoji) + " Suggestions using the ``suggest`` command will"
                                    +
                                    " now be posted and voted on in: " + channel.getAsMention());
            return commandEvent.replyEmbeds(eb.build()).complete();
        }

    }

    @Override
    public boolean hasPermission(final DiscordUser user, final Guild guild) {
        return user.hasPermissionInGuild(guild, PermissionHandler.PermissionLevel.STAFF);
    }
}