package club.mineplex.discord.commands.all.content;

import club.mineplex.core.discord.Embed;
import club.mineplex.core.discord.WebhookMessage;
import club.mineplex.discord.BotUtil;
import club.mineplex.discord.Main;
import club.mineplex.discord.objects.DiscordUser;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Data
public abstract class Content {

    private final DiscordUser sender;
    private final URL url;
    private final ContentType type;

    public Content(final DiscordUser sender, final URL url, final ContentType type) {
        this.sender = sender;
        this.url = url;
        this.type = type;
    }

    protected abstract void post();

    public static class Review extends Content {

        public Review(final DiscordUser sender, final URL url, final ContentType type) {
            super(sender, url, type);
        }

        @SneakyThrows
        public static void process(final Message message, final User processor, final ReviewResult result) {
            final List<MessageEmbed> embeds = message.getEmbeds();
            if (embeds.isEmpty()) {
                throw new IllegalArgumentException("This message is not a content review post!");
            }

            final MessageEmbed embed = embeds.get(0);
            final List<MessageEmbed.Field> fields = embed.getFields();
            if (fields.isEmpty()) {
                throw new IllegalArgumentException("This message is not a content review post!");
            }

            ContentType type = null;
            String url = null;

            for (final MessageEmbed.Field field : fields) {
                if (field.getName() == null || field.getValue() == null) {
                    continue;
                }

                final String name = field.getName();
                final String value = field.getValue();
                if (name.equals("Content Type")) {
                    type = ContentType.valueOf(value.replace("`", ""));
                } else if (name.equals("Link URL")) {
                    url = value;
                }

            }

            if (type == null || url == null) {
                throw new IllegalArgumentException("This message is not a content review post!");
            }

            final String author =
                    embed.getAuthor() == null || embed.getAuthor().getName() == null ? "" : embed.getAuthor().getName();
            final long userId = Long.parseLong(author.substring(author.lastIndexOf('(') + 1, author.lastIndexOf(')')));
            final User user = Main.getJDA().getUserById(userId);
            if (user == null) {
                throw new IllegalArgumentException("The content creator no longer exists!");
            }

            final String emojiMention = BotUtil.getEmoji(result.getEmoji()).getAsMention();
            final String resultName = BotUtil.prettifyString(result.name());

            final EmbedBuilder eb = new EmbedBuilder()
                    .setColor(result.getColor())
                    .setAuthor(String.format("%s#%s (%s)", user.getName(), user.getDiscriminator(), user.getId()),
                               user.getAvatarUrl()
                    )
                    .setDescription(String.format("%s `%s Submission`", emojiMention, resultName))
                    .addField("Content Type", String.format("`%s`", type.name()), false)
                    .addField("Link URL", url, false)
                    .addField("Processed By", processor.getAsMention(), false);

            message.editMessageEmbeds(Collections.singletonList(eb.build())).queue();
            message.clearReactions().queue();

            if (!result.equals(ReviewResult.REJECTED)) {
                new Content.Post(
                        new DiscordUser(user),
                        new URL(url),
                        type,
                        new DiscordUser(processor),
                        result.equals(ReviewResult.EXPLICIT)
                ).post();
            }
        }

        @Override
        protected final void post() {
            final Guild guild = Main.HOME_GUILD;
            if (guild == null) {
                throw new IllegalStateException("Home guild has not been loaded");
            }

            final TextChannel channel = guild.getTextChannelById(Main.contentReviewChannel);
            if (channel == null) {
                throw new RuntimeException("Content review channel does not exist!");
            }

            final User user = this.getSender().getUser();
            final EmbedBuilder eb = new EmbedBuilder()
                    .setColor(Color.CYAN)
                    .setAuthor(String.format("%s#%s (%s)", user.getName(), user.getDiscriminator(), user.getId()),
                               user.getAvatarUrl()
                    )
                    .addField("Content Type", String.format("`%s`", this.getType().name()), false)
                    .addField("Link URL", this.getUrl().toString(), false);

            final Message message = channel.sendMessageEmbeds(Collections.singletonList(eb.build())).complete();
            message.addReaction(BotUtil.getEmoji(Main.yesEmoji)).queue();
//            Removed because of Mineplex rules
//            message.addReaction(BotUtil.getEmoji(Main.neutralEmoji)).queue();
            message.addReaction(BotUtil.getEmoji(Main.noEmoji)).queue();
        }

    }

    @Getter
    public static class Post extends Content {

        private final DiscordUser processor;
        private final boolean explicit;

        public Post(final DiscordUser sender, final URL url, final ContentType type, final DiscordUser processor,
                    final boolean explicit) {
            super(sender, url, type);
            this.processor = processor;
            this.explicit = explicit;
        }

        @Override
        protected final void post() {
            final String webhook = Main.contentShowcaseWebhook;
            if (webhook == null) {
                return;
            }

            final String name = this.getSender().getUser().getName();
            final int id = new Random().nextInt(1000);
            final String title = String.format("%s%s POST! (ID: %s)", name, name.endsWith("s") ? "'" : "'s", id);

            final Role contentRole = Main.getJDA().getRoleById(Main.contentRole);
            final String mention = contentRole == null ? "" : contentRole.getAsMention() + "\n";

            WebhookMessage.builder()
                          .url(webhook)
                          .avatar_url(this.getType().getImageUrl().toString())
                          .content(mention + this.getUrl().toString())
                          .username(title)
                          .build()
                          .post();

            if (this.isExplicit()) {
                final Embed explicitEmbed = Embed.builder()
                                                 .color(Color.YELLOW)
                                                 .description("**WARNING:** *This video contains explicit content.*")
                                                 .build();

                WebhookMessage.builder()
                              .url(webhook)
                              .avatar_url(this.getType().getImageUrl().toString())
                              .embeds(new Embed[]{explicitEmbed})
                              .username(title)
                              .build()
                              .post();
            }
        }

    }

}
