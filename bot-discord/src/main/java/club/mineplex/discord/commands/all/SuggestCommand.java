package club.mineplex.discord.commands.all;

import club.mineplex.discord.BotUtil;
import club.mineplex.discord.Main;
import club.mineplex.discord.commands.IShop;
import club.mineplex.discord.commands.SlashCommand;
import club.mineplex.discord.objects.DiscordUser;
import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.receive.ReadonlyMessage;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.RestAction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class SuggestCommand extends SlashCommand implements IShop.Cooldownable {


    public SuggestCommand() {
        super("suggest", new OptionData(OptionType.STRING, "idea", "Detailed description of your idea", true));
    }

    @Override
    protected InteractionHook run(final DiscordUser sender, final SlashCommandEvent commandEvent) {

        final Optional<Guild> guildOpt = Optional.ofNullable(commandEvent.getGuild());
        final Optional<Member> memberOpt = Optional.ofNullable(commandEvent.getMember());
        if (!guildOpt.isPresent() || !memberOpt.isPresent()) {
            return this.error(commandEvent);
        }

        /* Looping through webhooks to see if suggestions is enabled */
        final String idea = Objects.requireNonNull(commandEvent.getOption("idea")).getAsString();
        final Guild guild = guildOpt.get();
        final Member member = memberOpt.get();
        final RestAction<List<Webhook>> ra = guild.retrieveWebhooks();
        final List<Webhook> webhooks = ra.complete();
        for (final Webhook webhook : webhooks) {

            /* Webhook was found */
            if (webhook.getName().equals("Suggestions")) {

                /* Checking if the user is blacklisted from making suggestions */
                final List<Long> blacklisted = Main.data.getOrDefault("blacklisted", List.class, new ArrayList<>());
                if (blacklisted.contains(member.getIdLong())) {
                    return commandEvent.replyEmbeds(new EmbedBuilder()
                                                            .setColor(Main.COLOR_RED)
                                                            .setDescription(BotUtil.getEmojiAsMention(Main.noEmoji)
                                                                                    + " You are ``blacklisted`` from making suggestions")
                                                            .build()).complete();
                }

                /* Sending suggestion */
                final Suggestion suggestion = new Suggestion(webhook, sender, idea);
                suggestion.send();

                /* Success message */
                final String authorTag = sender.getUser().getName() + "#" + sender.getUser().getDiscriminator();
                final EmbedBuilder success = new EmbedBuilder()
                        .setDescription(BotUtil.getEmojiAsMention(Main.yesEmoji)
                                                + " Your suggestion has been sent to " + webhook.getChannel()
                                                                                                .getAsMention()
                                                + " to be voted on.")
                        .setColor(Main.COLOR_GREEN);
                return commandEvent.replyEmbeds(success.build()).complete();
            }

        }

        /* Suggest hasn't been setup */
        final EmbedBuilder disabled = new EmbedBuilder()
                .setDescription(BotUtil.getEmojiAsMention(Main.noEmoji)
                                        + " The suggest feature hasn't been set up for this server.")
                .setColor(Main.COLOR_RED);
        return commandEvent.replyEmbeds(disabled.build()).complete();
    }

    @Override
    public double getCooldown() {
        return Main.config.getOrDefault("ideas-cooldown", Integer.class, 10);
    }

    /*
     * Suggestion Object
     */
    public static class Suggestion {

        private final DiscordUser author;
        private final String idea;
        private final Webhook serverHook;

        public Suggestion(final Webhook webhook, final DiscordUser author, final String idea) {
            this.serverHook = webhook;
            this.author = author;
            this.idea = idea;
        }

        public void send() {

            /* Commencing embed and webhook builder for suggestion */
            final WebhookClientBuilder builder = new WebhookClientBuilder(this.serverHook.getUrl());
            builder.setThreadFactory((job) -> {
                final Thread thread = new Thread(job);
                thread.setName("Suggestions");
                thread.setDaemon(true);
                return thread;
            });
            builder.setWait(true);
            final WebhookClient client = builder.build();


            /* Updating ideas counter in config */
            Main.data.reload();
            Main.data.set("idea-counter", Main.data.getOrDefault("idea-counter", Integer.class, 0) + 1);
            final int count = Main.data.getOrDefault("idea-counter", Integer.class, 0);

            /* Formatting data */
            final Date date = new Date();
            final String timezone = "EST";
            final DateFormat df = new SimpleDateFormat("yyyy/MM/dd | hh:mm a");
            df.setTimeZone(TimeZone.getTimeZone(timezone));
            final String discriminator = this.author.getUser().getDiscriminator();
            final WebhookEmbed embed = new WebhookEmbedBuilder()
                    .setColor(0x00cec9)
                    .setDescription(this.idea)
                    .setTitle(new WebhookEmbed.EmbedTitle("Mineplex Community Idea - #" + count, null))
                    .setFooter(new WebhookEmbed.EmbedFooter(
                            "Community Idea • " + df.format(date) + " (" + timezone + ") | " + discriminator,
                            Main.getJDA().getSelfUser().getAvatarUrl()
                    ))
                    .build();


            /* Building message embed and adding reaction */
            final WebhookMessageBuilder wmb = new WebhookMessageBuilder();
            wmb.setUsername(this.author.getUser().getName());
            wmb.addEmbeds(embed);
            wmb.setAvatarUrl(this.author.getUser().getAvatarUrl());
            try {
                final ReadonlyMessage rm = client.send(wmb.build()).get();
                this.serverHook.getChannel().addReactionById(rm.getId(), BotUtil.getEmoji(Main.yesEmoji)).queue();
                this.serverHook.getChannel().addReactionById(rm.getId(), BotUtil.getEmoji(Main.neutralEmoji)).queue();
                this.serverHook.getChannel().addReactionById(rm.getId(), BotUtil.getEmoji(Main.noEmoji)).queue();
            } catch (final Exception ignored) {
            }
            client.close();

        }

    }

}
